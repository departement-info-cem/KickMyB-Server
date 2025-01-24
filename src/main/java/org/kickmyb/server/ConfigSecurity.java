package org.kickmyb.server;

import org.kickmyb.server.account.ServiceAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
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
public class ConfigSecurity {

    @Autowired
    ServiceAccount userService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                authorize -> authorize
                        .requestMatchers("/api/id/**").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().permitAll()

                );
        return http.build();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService);
        return authenticationManagerBuilder.build();
    }


}