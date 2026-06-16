package org.kickmyb.serveur.tache;

import org.junit.jupiter.api.Test;
import org.kickmyb.serveur.notification.ServiceNotificationFirebase;
import org.kickmyb.serveur.utilisateur.DepotUtilisateur;
import org.kickmyb.serveur.utilisateur.MUtilisateur;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

import static org.mockito.Mockito.*;

@SpringBootTest
public class TestServicePlanificationNotifications {

    @Mock
    private DepotUtilisateur depotUtilisateur;

    @Mock
    private ServiceNotificationFirebase serviceNotificationFirebase;

    @InjectMocks
    private ServicePlanificationNotifications servicePlanification;

    @Test
    public void testVerifierTachesEcheanceDemain() {
        MockitoAnnotations.openMocks(this);

        // Créer un utilisateur avec une tâche qui arrive à échéance demain
        MUtilisateur utilisateur = new MUtilisateur();
        utilisateur.id = 1L;
        utilisateur.nom = "testuser";
        utilisateur.firebaseToken = "test-token";

        MTache tache = new MTache();
        tache.id = 1L;
        tache.nom = "Tâche test";
        tache.dateCreation = new Date();

        // Date limite demain
        LocalDate demain = LocalDate.now().plusDays(1);
        tache.dateLimite = Date.from(demain.atTime(12, 0).atZone(ZoneId.systemDefault()).toInstant());

        utilisateur.taches.add(tache);

        // Mock du repository
        when(depotUtilisateur.findAll()).thenReturn(Arrays.asList(utilisateur));

        // Exécuter la méthode de vérification
        servicePlanification.verifierTachesEcheanceDemain();

        // Vérifier que la notification a été envoyée
        verify(serviceNotificationFirebase, times(1)).envoyerNotificationTache(utilisateur, tache);
    }
}
