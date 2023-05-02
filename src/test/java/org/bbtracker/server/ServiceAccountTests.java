package org.bbtracker.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.kickmyb.server.ServerApplication;
import org.kickmyb.server.account.ServiceAccount;
import org.kickmyb.server.task.ServiceTask;
import org.kickmyb.transfer.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

// from https://www.baeldung.com/spring-boot-testing

@RunWith(SpringRunner.class)
// Lui indique ce qu'il faut qu'il mette en place pour faire les tests, ici tout car on a besoin de spring security
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
		classes = ServerApplication.class)
@TestPropertySource(
		locations = "classpath:application-integrationtest.properties")
class ServiceAccountTests {

	@Autowired
	private ServiceAccount serviceAccount;

	@Autowired
	private ServiceTask service;

	@Test
	void testNoDuplicate() {
		ServiceAccount.UsernameAlreadyTaken thrown =
				Assertions.assertThrows(ServiceAccount.UsernameAlreadyTaken.class, () -> {
					SignupRequest req = new SignupRequest();
					req.username = "test";
					req.password = "test";
					serviceAccount.signup(req);
					serviceAccount.signup(req);
		}, "NumberFormatException was expected");



	}

}
