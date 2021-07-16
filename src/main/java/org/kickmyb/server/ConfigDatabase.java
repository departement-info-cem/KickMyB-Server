package org.kickmyb.server;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Permet définition de la source de données utilisée par Spring data
 */
@Configuration
public class ConfigDatabase {
    
    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.h2.Driver");

        // On peut utiliser mémoire aussi quand on redémarre la BD est effacée
        //dataSourceBuilder.url("jdbc:h2:mem:test");
        dataSourceBuilder.url("jdbc:h2:./test");

        dataSourceBuilder.username("SA");
        dataSourceBuilder.password("");
        return dataSourceBuilder.build();
    }
}