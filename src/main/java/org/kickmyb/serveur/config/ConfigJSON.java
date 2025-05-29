package org.kickmyb.serveur.config;

import com.google.gson.Gson;
import org.kickmyb.GsonPersonnalise;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cette classe de configuration permet d'indiquer à Spring d'utiliser le Bean fourni quand il a besoin d'un Gson.
 *
 * Le serveur va donc utiliser notre objet Gson personnalisé quand il doit sérialiser en JSON.
 */
@Configuration
@ConditionalOnClass(Gson.class)
public class ConfigJSON {

    @Bean
    public Gson gson() {
        return GsonPersonnalise.gsonPerso();
    }

}
