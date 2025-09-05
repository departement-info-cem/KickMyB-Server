package org.kickmyb.serveur.tache;

import org.kickmyb.serveur.notification.ServiceNotificationFirebase;
import org.kickmyb.serveur.utilisateur.DepotUtilisateur;
import org.kickmyb.serveur.utilisateur.MUtilisateur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class ServicePlanificationNotifications {

    private static final Logger logger = LoggerFactory.getLogger(ServicePlanificationNotifications.class);

    @Autowired
    private DepotUtilisateur depotUtilisateur;

    @Autowired
    private ServiceNotificationFirebase serviceNotificationFirebase;

    /**
     * Vérifie chaque jour à 9h00 les tâches qui arrivent à échéance le lendemain
     * et envoie des notifications push aux utilisateurs concernés
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void verifierTachesEcheanceDemain() {
        logger.info("Début de la vérification des tâches arrivant à échéance demain");

        LocalDate demain = LocalDate.now().plusDays(1);
        Date debutDemain = Date.from(demain.atStartOfDay(ZoneId.systemDefault()).minusSeconds(1).toInstant());
        Date finDemain = Date.from(demain.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<MUtilisateur> tousUtilisateurs = depotUtilisateur.findAll();
        int notificationsEnvoyees = 0;

        for (MUtilisateur utilisateur : tousUtilisateurs) {
            for (MTache tache : utilisateur.taches) {
                if (tache.dateLimite != null && 
                    tache.dateLimite.after(debutDemain) &&
                    tache.dateLimite.before(finDemain)) {
                    
                    logger.info("Tâche '{}' de l'utilisateur '{}' arrive à échéance demain", 
                               tache.nom, utilisateur.nom);
                    
                    serviceNotificationFirebase.envoyerNotificationTache(utilisateur, tache);
                    notificationsEnvoyees++;
                }
            }
        }

        logger.info("Vérification terminée. {} notifications envoyées", notificationsEnvoyees);
    }
}
