package org.kickmyb.serveur.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class DelaiIntercepteur implements HandlerInterceptor {
    
    private static final long DELAI_MS = 1111;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Ajoute un délai artificiel à chaque requête
        Thread.sleep(DELAI_MS);
        return true; // Autorise la poursuite de l'exécution
    }
}