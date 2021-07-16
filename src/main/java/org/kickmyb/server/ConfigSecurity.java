package org.kickmyb.server;

import org.kickmyb.server.account.ServiceAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
public class ConfigSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    ServiceAccount userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin().and()// for h2 console only TODO remove for prod ?
                .csrf().disable()       // for a web API, disable CSRF token injected on response > request
                .cors().and()
                .authorizeRequests()
                // TODO quand on s'inscrit ou qu'on se connecte, on est pas encore dans le système
            .antMatchers("/api/id/**").permitAll()
                // TODO tous les autres appels à API requierent un utilisateur authentifié
            .antMatchers("/api/**").authenticated()
        ;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Bean @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // TODO changer le passwordEncoder et regarder ce que ça change en base de données
    @Bean public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
    //@Bean public PasswordEncoder passwordEncoder() {return NoOpPasswordEncoder.getInstance();}
}