package org.kickmyb.server;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kickmyb.server.account.BadCredentialsException;
import org.kickmyb.server.account.MUser;
import org.kickmyb.server.account.ServiceAccount;
import org.kickmyb.server.task.ServiceTask;
import org.kickmyb.transfer.AddTaskRequest;
import org.kickmyb.transfer.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

// TODO pour celui-ci on aimerait pouvoir mocker l'utilisateur pour ne pas avoir à le créer
// https://reflectoring.io/spring-boot-mock/#:~:text=This%20is%20easily%20done%20by,our%20controller%20can%20use%20it.

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = KickMyBServerApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)

class ServiceTaskTests {

    @Autowired
    private ServiceTask serviceTask;

    @Autowired
    private ServiceAccount serviceAccount;

    @Test
    void testAjouterTacheOk() throws ServiceTask.Empty, ServiceTask.TooShort, ServiceTask.Existing,
            ServiceAccount.UsernameTooShort, ServiceAccount.PasswordTooShort,
            ServiceAccount.UsernameAlreadyTaken, BadCredentialsException {

        // on crée un compte
        SignupRequest req = new SignupRequest();
        req.username = "alice";
        req.password = "Passw0rd!";
        serviceAccount.signup(req);

        // on récupère l'utilisateur
        MUser alice = serviceTask.userFromUsername("alice");

        // on crée une tâche
        AddTaskRequest addTaskRequest = new AddTaskRequest();
        addTaskRequest.name = "Tâche 1";
        addTaskRequest.deadline = Date.from(new Date().toInstant().plusSeconds(3600));

        // on ajoute la tâche à l'utilisateur
        serviceTask.addOne(addTaskRequest, alice);

        // on vérifie que la tâche a bien été ajoutée
        assertEquals(1, serviceTask.home(alice.id).size());
    }

    @Test
    void testAjouterTacheNomVideKo() throws ServiceAccount.UsernameTooShort, ServiceAccount.PasswordTooShort,
            ServiceAccount.UsernameAlreadyTaken, BadCredentialsException {

        // on crée un compte
        SignupRequest req = new SignupRequest();
        req.username = "alice";
        req.password = "Passw0rd!";
        serviceAccount.signup(req);

        // on récupère l'utilisateur
        MUser alice = serviceTask.userFromUsername("alice");

        // on crée une tâche avec un nom vide
        AddTaskRequest addTaskRequest = new AddTaskRequest();
        addTaskRequest.name = "";
        addTaskRequest.deadline = Date.from(new Date().toInstant().plusSeconds(3600));

        // on essaie d'ajouter la tâche à l'utilisateur
        try{
            serviceTask.addOne(addTaskRequest, alice);
        } catch (Exception e) {
        }
        // on vérifie que la tâche n'a pas été ajoutée
        assertEquals(0, serviceTask.home(alice.id).size());
    }

    @Test
    void testAjouterTacheNomTropCourtKo() throws ServiceAccount.UsernameTooShort, ServiceAccount.PasswordTooShort,
            ServiceAccount.UsernameAlreadyTaken, BadCredentialsException {

        // on crée un compte
        SignupRequest req = new SignupRequest();
        req.username = "alice";
        req.password = "Passw0rd!";
        serviceAccount.signup(req);

        // on récupère l'utilisateur
        MUser alice = serviceTask.userFromUsername("alice");

        // on crée une tâche avec un nom trop court
        AddTaskRequest addTaskRequest = new AddTaskRequest();
        addTaskRequest.name = "t";
        addTaskRequest.deadline = Date.from(new Date().toInstant().plusSeconds(3600));

        // on essaie d'ajouter la tâche à l'utilisateur
        try{
            serviceTask.addOne(addTaskRequest, alice);
        } catch (Exception e) {
        }
        // on vérifie que la tâche n'a pas été ajoutée
        assertEquals(0, serviceTask.home(alice.id).size());
    }

    @Test
    void testAjouterTacheNomExistantKo() throws ServiceTask.Empty, ServiceTask.TooShort, ServiceTask.Existing,
            ServiceAccount.UsernameTooShort, ServiceAccount.PasswordTooShort,
            ServiceAccount.UsernameAlreadyTaken, BadCredentialsException {

        // on crée un compte
        SignupRequest req = new SignupRequest();
        req.username = "alice";
        req.password = "Passw0rd!";
        serviceAccount.signup(req);

        // on récupère l'utilisateur
        MUser alice = serviceTask.userFromUsername("alice");

        // on crée 2 tâches avec le même nom
        AddTaskRequest addTaskRequest1 = new AddTaskRequest();
        AddTaskRequest addTaskRequest2 = new AddTaskRequest();
        addTaskRequest1.name = "Tâche 1";
        addTaskRequest2.name = "Tâche 1";
        addTaskRequest1.deadline = Date.from(new Date().toInstant().plusSeconds(3600));
        addTaskRequest2.deadline = Date.from(new Date().toInstant().plusSeconds(3600));

        // on ajoute la tâche 1 à l'utilisateur
        serviceTask.addOne(addTaskRequest1, alice);

        // on vérifie que la tâche a bien été ajoutée
        assertEquals(1, serviceTask.home(alice.id).size());

        // on essaie d'ajouter la tâche 2 à l'utilisateur
        try{
            serviceTask.addOne(addTaskRequest2, alice);
        } catch (Exception e) {
        }
        // on vérifie que la tâche 2 n'a pas été ajoutée
        assertEquals(1, serviceTask.home(alice.id).size());
    }
}
