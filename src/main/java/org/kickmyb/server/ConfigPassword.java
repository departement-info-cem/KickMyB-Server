package org.kickmyb.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ConfigPassword {

    // TODO changer le passwordEncoder et regarder ce que ça change en base de données

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
    //@Bean public PasswordEncoder passwordEncoder() {return NoOpPasswordEncoder.getInstance();}
}
