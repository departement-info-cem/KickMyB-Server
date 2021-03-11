package org.kickmyb.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// le but est de forcer un seul format compatible avec Java de Android par défaut
// https://github.com/google/gson/blob/master/gson/src/main/java/com/google/gson/internal/bind/DateTypeAdapter.java


@Configuration
@ConditionalOnClass(Gson.class)
public class ConfigurationJSON {

    @Bean
    public Gson gson() {
        String format = "MMM d, yyyy hh:mm:ss a";
        Gson result = new GsonBuilder().setDateFormat(format).create();
        return result;
    }

}
