package org.kickmyb.serveur.notification;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.kickmyb.serveur.tache.MTache;
import org.kickmyb.serveur.utilisateur.MUtilisateur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ServiceNotificationFirebase {

    private static final Logger logger = LoggerFactory.getLogger(ServiceNotificationFirebase.class);

    public void envoyerNotificationTache(MUtilisateur utilisateur, MTache tache) {
        if (utilisateur.firebaseToken == null || utilisateur.firebaseToken.trim().isEmpty()) {
            logger.warn("Aucun token Firebase pour l'utilisateur {}", utilisateur.nom);
            return;
        }

        try {
            Notification notification = Notification.builder()
                    .setTitle("Rappel de tâche")
                    .setBody("La tâche '" + tache.nom + "' arrive à échéance demain !")
                    .build();

            Message message = Message.builder()
                    .setToken(utilisateur.firebaseToken)
                    .setNotification(notification)
                    .putData("tacheId", tache.id.toString())
                    .putData("type", "rappel_tache")
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            logger.info("Notification envoyée avec succès à {} pour la tâche '{}': {}", 
                       utilisateur.nom, tache.nom, response);
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de la notification à {} pour la tâche '{}': {}", 
                        utilisateur.nom, tache.nom, e.getMessage(), e);
        }
    }
}
