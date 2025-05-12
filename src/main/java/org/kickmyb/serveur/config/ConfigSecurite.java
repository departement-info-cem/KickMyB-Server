package org.kickmyb.serveur.config;

import org.kickmyb.serveur.utilisateur.ServiceUtilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

// TODO security

/**
 * Maintenant /api/home n'est pas sécurisé /api/index l'est
 * il faudrait faire un userService utilisant la BD
 * il faudrait faire un autres webservice pour signup et signin/signout
 */

/**
 * Par défaut, Spring attribue une session gérée en RAM par le serveur
 * Le cookie envoyé au client contient simplement d'ID de la session
 *
 * Spring security attache un context de security à la session qui contient l'utilisateur courant
 * Il y a un filtre qui à réception d'une requête
 * 1 cherche le cookie
 * 2 trouve la session y correspondant
 * 3 peuple le SecurityContext et le rend accessible via le SecurityContextHolder (accesseur static getContext())
 *
 * Ça repose sur les sessions donc, ça ne marche pas si le serveur redémarre ou s'il y a plusieurs serveurs
 *
 * Du coup, on peut aussi avoir un JWT en faisant notre propre filtre pour injecter le SecurityContext
 */


/**
 * Sources
 *
 * https://docs.spring.io/spring-security/site/docs/3.1.x/reference/technical-overview.html
 * https://docs.spring.io/spring-security/site/docs/3.1.x/reference/core-services.html#core-services-authentication-manager
 * https://docs.spring.io/spring-security/site/docs/4.2.x/reference/htmlsingle/#multiple-httpsecurity
 * https://docs.spring.io/spring-security/site/docs/4.2.19.RELEASE/guides/html5/helloworld-boot.html
 *
 * https://www.youtube.com/watch?v=X80nJ5T7YpE
 * https://www.okta.com/identity-101/what-is-token-based-authentication/
 * https://www.baeldung.com/registration-with-spring-mvc-and-spring-security
 *
 * Il n'y a pas d'authentification sans état, la vraie question est où se trouve l'état
 * https://blog.jdriven.com/2014/10/stateless-spring-security-part-2-stateless-authentication/
 * https://spring.io/guides/tutorials/spring-security-and-angular-js/
 * https://skillsmatter.com/skillscasts/5398-the-state-of-securing-restful-apis-with-spring
 */

// TODO various authentication session, token, JWT, authentication provider OAuth

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
public class ConfigSecurite {

    @Autowired
    ServiceUtilisateur userService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                authorize -> authorize
                        // signin signup signout doivent être accessibles sans être connecté
                        .requestMatchers("/id/**").permitAll()
                        // tous les appels à l'API doit être fait quand connecté
                        .requestMatchers("/tache/**").authenticated()
                        // nécessaire pour que Spring laisse passer les requêtes pour h2-console
                        .requestMatchers("/h2-console/**").permitAll()
                        // nécessaire pour faire fonctionner / et les démos MVC
                        .anyRequest().permitAll()

                );
        return http.build();
    }

    /**
     * Ce bean est nécessaire pour indiquer à Spring Security comment stocker les Context de Security
     * Ici il va utiliser la session HTTP en RAM sur le serveur qui est attachée au Cookie JSESSIONID
     * @return
     */
    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    /**
     * Ce Bean indique à Spring que notre service (userService) est responsable du rôle de
     * userDetailsService.
     *
     * Essentiellement on veut :
     * - que quand Spring Security a besoin de UserDetails via la fonction loadUserByUsername
     * - notre service implante cette fonction
     * - et qu'on puisse alors trouver l'utilisateur dans notre BD
     *
     * Ce genre de bean est nécessaire dès qu'on veut stocker les comptes dans une BD sous
     * notre contrôle et pas gérée par Spring Security.
     */
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService);
        return authenticationManagerBuilder.build();
    }


}