package org.kickmyb.serveur.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Ce Bean va être utilisé à chaque fois que le serveur a besoin d'un objet de type PasswordEncoder.
 *
 * Cela va permettre de changer automatiquement l'encodage de mot de passe dès que Spring Security
 * et le AuthenticationManager va avoir besoin d'encoder un mot de passe.
 *
 * tout le monde Java tourne autour des termes de café donc un Java bean est une référence à un grain de café
 */
@Configuration
public class ConfigMotDePasse {

    // TODO changer le passwordEncoder et regarder ce que ça change en base de données
    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
    //@Bean public PasswordEncoder passwordEncoder() {return NoOpPasswordEncoder.getInstance();}
}
