package org.kickmyb.serveur;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kickmyb.serveur.utilisateur.ServiceUtilisateur;
import org.kickmyb.transfer.RequeteInscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

// from https://www.baeldung.com/spring-boot-testing

@ExtendWith(SpringExtension.class)
// Lui indique ce qu'il faut qu'il mette en place pour faire les tests, ici tout car on a besoin de spring security
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
		classes = KickMyBServerApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class TestsServiceUtilisateur {

	@Autowired
	private ServiceUtilisateur serviceAccount;

	@Test
	void testNoDuplicate() {
		ServiceUtilisateur.NomDejaPris thrown =
				Assertions.assertThrows(ServiceUtilisateur.NomDejaPris.class, () -> {
					RequeteInscription req = new RequeteInscription();
					req.nom = "test";
					req.motDePasse = "test";
					req.confirmationMotDePasse = "test";
					serviceAccount.inscrire(req);
					serviceAccount.inscrire(req);
		}, "Username Taken was expected");
	}

	@Test
	void testInscrireAndSignin()  {
		{
			RequeteInscription req = new RequeteInscription();
			req.nom = "marie";
			req.motDePasse = "test";
			req.confirmationMotDePasse = "test";
			serviceAccount.inscrire(req);
		}
		{
			UserDetails ud = serviceAccount.loadUserByUsername("marie");
			Assertions.assertNotNull(ud);
			Assertions.assertEquals(ud.getUsername(), "marie");
		}
	}

}
