package com.tecsup.app.micro.order.integration;

import com.tecsup.app.micro.order.domain.model.OrderStatus;
import com.tecsup.app.micro.order.infrastructure.persistence.entity.OrderEntity;
import com.tecsup.app.micro.order.infrastructure.persistence.entity.OrderItemEntity;
import com.tecsup.app.micro.order.infrastructure.persistence.repository.OrderJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
class OrderServiceContainersIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

    @Container
    static final KafkaContainer KAFKA = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.1"));

    @DynamicPropertySource
    static void registerProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.kafka.bootstrap-servers", KAFKA::getBootstrapServers);
        registry.add("spring.kafka.listener.auto-startup", () -> "false");
        registry.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri", () -> "http://localhost:9999/mock-jwks");
        registry.add("clients.catalog-service.url", () -> "http://localhost:8082");
    }

    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @Test
    void shouldPersistOrderAndItemsOnPostgresContainer() {
        OrderEntity order = OrderEntity.builder()
                .customerAuthUserId("sub-integration")
                .restaurantId(1L)
                .status(OrderStatus.CREATED)
                .totalAmount(new BigDecimal("30.00"))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        OrderItemEntity item = OrderItemEntity.builder()
                .order(order)
                .productId(10L)
                .productName("Integracion Pizza")
                .unitPrice(new BigDecimal("30.00"))
                .quantity(1)
                .subtotal(new BigDecimal("30.00"))
                .build();
        order.setItems(List.of(item));

        var saved = orderJpaRepository.save(order);

        assertTrue(saved.getId() != null && saved.getId() > 0);
        assertTrue(orderJpaRepository.findById(saved.getId()).isPresent());
    }
}
