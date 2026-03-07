package com.tecsup.app.micro.catalog.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI catalogMicroserviceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Catalog Microservice API")
                        .description("API para la gestión de Restaurantes y Productos del Catálogo")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Tecsup Team")
                                .email("backend@tecsup.app")));
    }
}