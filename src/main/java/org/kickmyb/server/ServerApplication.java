package org.kickmyb.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;

// TODO implémenter le paging pour une liste très longue ????
// TODO regarder déploiement sur AWS


@SpringBootApplication(exclude = {JacksonAutoConfiguration.class})
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
