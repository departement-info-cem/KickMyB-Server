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

// TODO pour celui ci on aimerait pouvoir mocker l'utilisateur pour ne pas avoir à le créer

// https://reflectoring.io/spring-boot-mock/#:~:text=This%20is%20easily%20done%20by,our%20controller%20can%20use%20it.

@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
		classes = ServerApplication.class)
@TestPropertySource(
		locations = "classpath:application-integrationtest.properties")
class ServiceTaskTests {

	@Autowired
	private ServiceAccount serviceAccount;

	@Autowired
	private ServiceTask serviceTask;


	@Test
	void testNoDuplicateTasks() {
		// TODO

	}

}
