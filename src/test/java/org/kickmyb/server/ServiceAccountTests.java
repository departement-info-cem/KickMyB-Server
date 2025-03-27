package org.kickmyb.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kickmyb.server.account.BadCredentialsException;
import org.kickmyb.server.account.ServiceAccount;
import org.kickmyb.server.task.ServiceTask;
import org.kickmyb.transfer.SigninRequest;
import org.kickmyb.transfer.SignupRequest;
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
class ServiceAccountTests {

	@Autowired
	private ServiceAccount serviceAccount;

	@Test
	void testNoDuplicate() {
		ServiceAccount.UsernameAlreadyTaken thrown =
				Assertions.assertThrows(ServiceAccount.UsernameAlreadyTaken.class, () -> {
					SignupRequest req = new SignupRequest();
					req.username = "test";
					req.password = "test";
					serviceAccount.signup(req);
					serviceAccount.signup(req);
		}, "Username Taken was expected");
	}

	@Test
	void testSignupAndSignin() throws ServiceAccount.UsernameTooShort, ServiceAccount.PasswordTooShort, ServiceAccount.UsernameAlreadyTaken, BadCredentialsException {
		{
			SignupRequest req = new SignupRequest();
			req.username = "marie";
			req.password = "test";
			serviceAccount.signup(req);
		}
		{
			UserDetails ud = serviceAccount.loadUserByUsername("marie");
			Assertions.assertNotNull(ud);
			Assertions.assertEquals(ud.getUsername(), "marie");
		}
	}

}
