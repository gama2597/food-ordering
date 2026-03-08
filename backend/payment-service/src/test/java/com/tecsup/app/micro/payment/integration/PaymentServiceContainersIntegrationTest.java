package com.tecsup.app.micro.payment.integration;

import com.tecsup.app.micro.payment.domain.model.PaymentStatus;
import com.tecsup.app.micro.payment.infrastructure.persistence.entity.PaymentEntity;
import com.tecsup.app.micro.payment.infrastructure.persistence.repository.PaymentJpaRepository;
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

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
class PaymentServiceContainersIntegrationTest {

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
    }

    @Autowired
    private PaymentJpaRepository paymentJpaRepository;

    @Test
    void shouldPersistPaymentOnPostgresContainer() {
        PaymentEntity entity = PaymentEntity.builder()
                .orderId(101L)
                .customerAuthUserId("sub-integration")
                .amount(new BigDecimal("44.00"))
                .currency("PEN")
                .status(PaymentStatus.APPROVED)
                .reason("Pago aprobado")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        var saved = paymentJpaRepository.save(entity);

        assertTrue(saved.getId() != null && saved.getId() > 0);
        assertTrue(paymentJpaRepository.findByOrderId(101L).isPresent());
    }
}
