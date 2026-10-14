package com.dawan.cinema.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * Configuration de la documentation OpenAPI (Swagger UI).
 * Aucun serveur n'est déclaré : Swagger utilise l'URL courante,
 * ce qui fonctionne quel que soit le port configuré.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI().info(new Info()
                .title("CC8 Spring Cinema API")
                .version("1.0")
                .description("API de gestion d'un groupe de cinémas : cinémas, salles, films et séances."));
    }
}