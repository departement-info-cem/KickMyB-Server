package org.kickmyb.serveur.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "KickMyB API",
        description = "API pour kick my b",
        version = "1.0"
    )
)
public class ConfigSwagger {
}