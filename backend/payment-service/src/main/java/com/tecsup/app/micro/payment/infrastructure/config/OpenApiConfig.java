package com.tecsup.app.micro.payment.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI paymentServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Payment Microservice API")
                        .description("API para gestion y consulta de pagos")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Tecsup Team")
                                .email("backend@tecsup.app")));
    }
}
