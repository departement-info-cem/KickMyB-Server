	package org.kickmyb.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;

// TODO utiliser Spring security pour gérer les comptes
// TODO implémenter le paging pour une liste très longue ????
// TODO compléter événements pour un bébé
// TODO regarder déploiement sur AWS ou Heroku ou autre

	
@SpringBootApplication(exclude = {JacksonAutoConfiguration.class})
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
