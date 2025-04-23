package org.kickmyb.server;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

/**
 * La déclaration @Component indique à Spring de charger cette classe et d'en tenir compte dans la
 * configuration du serveur.
 *
 * attenteArtificielle est une méthode qui sera appelée dans chaque contrôleur pour patienter 1500 ms
 * et tester les indicateurs d'attentde de notre GUI
 *
 * customize permet de spécifier des paramètres du serveur au démarrage comme un port spécifique
 * On pourrait également configurer ces éléments via le application.properties
 */
@Component
public class ConfigHTTP
        implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    // TODO replace with a proper interceptor
    public static void attenteArticifielle(){
        try {Thread.sleep(500);} catch (InterruptedException e) {}
    }

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        // Config du certification auto-signé
//        Ssl ssl = new Ssl();
//        ssl.setKeyPassword("password");
//        ssl.setKeyStore("keystore.jks");
//        factory.setSsl(ssl);
        // TODO à changer pour modifier le port du serveur
//        factory.setPort(8787);
    }
}