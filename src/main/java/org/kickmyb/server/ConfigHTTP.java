package org.kickmyb.server;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

// Composant scanné et intégrer via Spring
@Component
public class ConfigHTTP
        implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    // TODO replace with a proper interceptor
    public static void attenteArticifielle(){
        try {Thread.sleep(0);} catch (InterruptedException e) {}
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