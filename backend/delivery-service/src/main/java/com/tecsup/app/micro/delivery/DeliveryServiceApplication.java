package com.tecsup.app.micro.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Punto de entrada del microservicio. Inicia el contexto Spring Boot y el ciclo de vida de la aplicacion.
 */
@SpringBootApplication
@EnableKafka
public class DeliveryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliveryServiceApplication.class, args);
    }
}

