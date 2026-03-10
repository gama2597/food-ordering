package com.tecsup.app.micro.user.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion tecnica del microservicio (seguridad, OpenAPI, Kafka o integraciones de infraestructura).
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI userServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Microservice API")
                        .description("API para gestion de perfil y direcciones del usuario")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Tecsup Team")
                                .email("backend@tecsup.app")));
    }
}

