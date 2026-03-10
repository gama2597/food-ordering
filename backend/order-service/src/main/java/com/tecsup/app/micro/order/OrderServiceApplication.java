package com.tecsup.app.micro.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Punto de entrada del microservicio. Inicia el contexto Spring Boot y el ciclo de vida de la aplicacion.
 */
@SpringBootApplication
@EnableFeignClients
@EnableKafka
public class OrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

}

