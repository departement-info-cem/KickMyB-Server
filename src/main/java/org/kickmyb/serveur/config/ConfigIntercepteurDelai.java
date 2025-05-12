package org.kickmyb.serveur.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConfigIntercepteurDelai implements WebMvcConfigurer {

    @Autowired
    private DelaiIntercepteur delaiIntercepteur;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(delaiIntercepteur);
    }
}