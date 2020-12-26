package org.kickmyb.server;

import org.glassfish.jersey.server.ResourceConfig;
import org.kickmyb.server.id.WebServiceID;
import org.kickmyb.server.jersey.CatchAllMapper;
import org.kickmyb.server.jersey.GSONMapper;
import org.kickmyb.server.tasks.WebServiceCore;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

// Ce composant permet à Spring d'intégrer les éléments de Jersey
// Chaque service de Jersey et lui même détecter via une annotation

// TODO enlever jersey Spring a déjà ce qu'il faut donc...  Exception mapping and Gson

@Component
@ApplicationPath("/exo/*")                              // TODO change to fix the prefix for all services, remove if none
public class JerseyComponent extends ResourceConfig {
    public JerseyComponent() {
        // register each class that has endpoints in them
        //register(WebServiceID.class);
        //register(WebServiceCore.class);
        register(GSONMapper.class);
        register(CatchAllMapper.class);
        // Will make deployment fail but not local run come on man!!!
        //packages("org.kickmyb.server");        // TODO change if you change your package name
    }
}
