package com.tecsup.app.micro.delivery.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI deliveryServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Delivery Microservice API")
                        .description("API para seguimiento de entregas")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Tecsup Team")
                                .email("backend@tecsup.app")));
    }
}
