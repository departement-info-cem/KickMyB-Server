package org.kickmyb.serveur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;

@SpringBootApplication(exclude = {JacksonAutoConfiguration.class})
public class KickMyBServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(KickMyBServerApplication.class, args);
	}

}