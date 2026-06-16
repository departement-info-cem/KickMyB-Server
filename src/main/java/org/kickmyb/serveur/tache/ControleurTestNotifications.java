package org.kickmyb.serveur.tache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControleurTestNotifications {

    @Autowired
    private ServicePlanificationNotifications servicePlanification;

    @PostMapping("/test/notifications")
    public String testerNotifications() {
        try {
            servicePlanification.verifierTachesEcheanceDemain();
            return "Vérification des notifications terminée avec succès";
        } catch (Exception e) {
            return "Erreur lors de la vérification des notifications: " + e.getMessage();
        }
    }
}
