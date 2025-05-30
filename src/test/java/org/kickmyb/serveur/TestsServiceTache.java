package org.kickmyb.serveur;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kickmyb.serveur.tache.ServiceTache;
import org.kickmyb.serveur.utilisateur.MUtilisateur;
import org.kickmyb.serveur.utilisateur.ServiceUtilisateur;
import org.kickmyb.transfer.RequeteAjoutTache;
import org.kickmyb.transfer.RequeteInscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

// TODO pour celui-ci on aimerait pouvoir mocker l'utilisateur pour ne pas avoir à le créer
// https://reflectoring.io/spring-boot-mock/#:~:text=This%20is%20easily%20done%20by,our%20controller%20can%20use%20it.

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = KickMyBServerApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)

class TestsServiceTache {

    @Autowired
    private ServiceTache serviceTache;

    @Autowired
    private ServiceUtilisateur serviceAccount;

    @Test
    void testAjouterTacheOk() throws ServiceTache.Vide, ServiceTache.Existant, ServiceTache.TropCourt, ServiceUtilisateur.NomTropCourt, ServiceUtilisateur.MotDePasseTropCourt, ServiceUtilisateur.NomDejaPris, ServiceUtilisateur.MotsDePasseDifferents {

        // on crée un compte
        RequeteInscription req = new RequeteInscription();
        req.nom = "alice";
        req.motDePasse = "Passw0rd!";
        req.confirmationMotDePasse = "Passw0rd!";
        serviceAccount.inscrire(req);

        // on récupère l'utilisateur
        MUtilisateur alice = serviceTache.utilisateurParSonNom("alice");

        // on crée une tâche
        RequeteAjoutTache addTaskRequest = new RequeteAjoutTache();
        addTaskRequest.nom = "Tâche 1";
        addTaskRequest.dateLimite = Date.from(new Date().toInstant().plusSeconds(3600));

        // on ajoute la tâche à l'utilisateur
        serviceTache.ajouteUneTache(addTaskRequest, alice.id);

        // on vérifie que la tâche a bien été ajoutée
        assertEquals(1, serviceTache.accueil(alice.id).size());
    }

    @Test
    void testAjouterTacheNomVideKo() throws ServiceUtilisateur.NomTropCourt, ServiceUtilisateur.MotDePasseTropCourt,
            ServiceUtilisateur.NomDejaPris, ServiceUtilisateur.MotsDePasseDifferents {

        // on crée un compte
        RequeteInscription req = new RequeteInscription();
        req.nom = "alice";
        req.motDePasse = "Passw0rd!";
        req.confirmationMotDePasse = "Passw0rd!";
        serviceAccount.inscrire(req);

        // on récupère l'utilisateur
        MUtilisateur alice = serviceTache.utilisateurParSonNom("alice");

        // on crée une tâche avec un nom vide
        RequeteAjoutTache addTaskRequest = new RequeteAjoutTache();
        addTaskRequest.nom = "";
        addTaskRequest.dateLimite = Date.from(new Date().toInstant().plusSeconds(3600));

        // on essaie d'ajouter la tâche à l'utilisateur
        try{
            serviceTache.ajouteUneTache(addTaskRequest, alice.id);
        } catch (Exception e) {
        }
        // on vérifie que la tâche n'a pas été ajoutée
        assertEquals(0, serviceTache.accueil(alice.id).size());
    }

    @Test
    void testAjouterTacheNomTropCourtKo() throws ServiceUtilisateur.NomTropCourt, ServiceUtilisateur.MotDePasseTropCourt,
            ServiceUtilisateur.NomDejaPris, ServiceUtilisateur.MotsDePasseDifferents {

        // on crée un compte
        RequeteInscription req = new RequeteInscription();
        req.nom = "alice";
        req.motDePasse = "Passw0rd!";
        req.confirmationMotDePasse = "Passw0rd!";
        serviceAccount.inscrire(req);

        // on récupère l'utilisateur
        MUtilisateur alice = serviceTache.utilisateurParSonNom("alice");

        // on crée une tâche avec un nom trop court
        RequeteAjoutTache addTaskRequest = new RequeteAjoutTache();
        addTaskRequest.nom = "t";
        addTaskRequest.dateLimite = Date.from(new Date().toInstant().plusSeconds(3600));

        // on essaie d'ajouter la tâche à l'utilisateur
        try{
            serviceTache.ajouteUneTache(addTaskRequest, alice.id);
        } catch (Exception e) {
        }
        // on vérifie que la tâche n'a pas été ajoutée
        assertEquals(0, serviceTache.accueil(alice.id).size());
    }

    @Test
    void testAjouterTacheNomExistantKo() throws ServiceTache.Vide, ServiceTache.TropCourt, ServiceTache.Existant,
            ServiceUtilisateur.NomTropCourt, ServiceUtilisateur.MotDePasseTropCourt,
            ServiceUtilisateur.NomDejaPris, ServiceUtilisateur.MotsDePasseDifferents {

        // on crée un compte
        RequeteInscription req = new RequeteInscription();
        req.nom = "alice";
        req.motDePasse = "Passw0rd!";
        req.confirmationMotDePasse = "Passw0rd!";
        serviceAccount.inscrire(req);

        // on récupère l'utilisateur
        MUtilisateur alice = serviceTache.utilisateurParSonNom("alice");

        // on crée 2 tâches avec le même nom
        RequeteAjoutTache addTaskRequest1 = new RequeteAjoutTache();
        RequeteAjoutTache addTaskRequest2 = new RequeteAjoutTache();
        addTaskRequest1.nom = "Tâche 1";
        addTaskRequest2.nom = "Tâche 1";
        addTaskRequest1.dateLimite = Date.from(new Date().toInstant().plusSeconds(3600));
        addTaskRequest2.dateLimite = Date.from(new Date().toInstant().plusSeconds(3600));

        // on ajoute la tâche 1 à l'utilisateur
        serviceTache.ajouteUneTache(addTaskRequest1, alice.id);

        // on vérifie que la tâche a bien été ajoutée
        assertEquals(1, serviceTache.accueil(alice.id).size());

        // on essaie d'ajouter la tâche 2 à l'utilisateur
        try{
            serviceTache.ajouteUneTache(addTaskRequest2, alice.id);
        } catch (Exception e) {
        }
        // on vérifie que la tâche 2 n'a pas été ajoutée
        assertEquals(1, serviceTache.accueil(alice.id).size());
    }
}
