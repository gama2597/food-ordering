package com.tecsup.app.micro.payment.contract;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tecsup.app.micro.payment.infrastructure.messaging.event.PaymentApprovedEvent;
import com.tecsup.app.micro.payment.infrastructure.messaging.event.PaymentRejectedEvent;
import com.tecsup.app.micro.payment.infrastructure.messaging.event.PaymentRequestedEvent;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentEventContractTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    @Test
    void shouldDeserializePaymentRequestedPayloadFromContract() throws Exception {
        String json = """
                {
                  "orderId": 101,
                  "customerAuthUserId": "f2f65f52-8f58-4fa4-a09d-00e4e6f18f1d",
                  "requestedAt": "2026-03-08T12:10:35Z",
                  "amount": 45.50,
                  "currency": "PEN"
                }
                """;

        PaymentRequestedEvent event = objectMapper.readValue(json, PaymentRequestedEvent.class);

        assertThat(event.orderId()).isEqualTo(101L);
        assertThat(event.customerAuthUserId()).isEqualTo("f2f65f52-8f58-4fa4-a09d-00e4e6f18f1d");
        assertThat(event.amount()).isEqualByComparingTo(new BigDecimal("45.50"));
        assertThat(event.currency()).isEqualTo("PEN");
        assertThat(event.requestedAt()).isEqualTo(Instant.parse("2026-03-08T12:10:35Z"));
    }

    @Test
    void shouldSerializePaymentApprovedWithRequiredFields() throws Exception {
        PaymentApprovedEvent event = new PaymentApprovedEvent(
                "f17f5906-f63c-4e40-b8e6-1f1a53bd4b8e",
                1,
                "PAYMENT_APPROVED",
                Instant.parse("2026-03-08T12:10:37Z"),
                101L,
                5001L,
                "f2f65f52-8f58-4fa4-a09d-00e4e6f18f1d",
                new BigDecimal("45.50"),
                "PEN",
                "APPROVED",
                "Pago aprobado"
        );

        JsonNode root = objectMapper.readTree(objectMapper.writeValueAsString(event));

        assertThat(root.get("eventVersion").asInt()).isEqualTo(1);
        assertThat(root.get("eventType").asText()).isEqualTo("PAYMENT_APPROVED");
        assertThat(root.get("orderId").asLong()).isEqualTo(101L);
        assertThat(root.get("paymentId").asLong()).isEqualTo(5001L);
        assertThat(root.get("status").asText()).isEqualTo("APPROVED");
        assertThat(root.get("amount").decimalValue()).isEqualByComparingTo(new BigDecimal("45.50"));
    }

    @Test
    void shouldSerializePaymentRejectedWithRequiredFields() throws Exception {
        PaymentRejectedEvent event = new PaymentRejectedEvent(
                "d476cd1d-4af9-4976-a318-fce0f5518ad4",
                1,
                "PAYMENT_REJECTED",
                Instant.parse("2026-03-08T12:10:37Z"),
                101L,
                5002L,
                "f2f65f52-8f58-4fa4-a09d-00e4e6f18f1d",
                new BigDecimal("45.50"),
                "PEN",
                "REJECTED",
                "Pago rechazado por simulacion"
        );

        JsonNode root = objectMapper.readTree(objectMapper.writeValueAsString(event));

        assertThat(root.get("eventVersion").asInt()).isEqualTo(1);
        assertThat(root.get("eventType").asText()).isEqualTo("PAYMENT_REJECTED");
        assertThat(root.get("orderId").asLong()).isEqualTo(101L);
        assertThat(root.get("paymentId").asLong()).isEqualTo(5002L);
        assertThat(root.get("status").asText()).isEqualTo("REJECTED");
        assertThat(root.get("amount").decimalValue()).isEqualByComparingTo(new BigDecimal("45.50"));
    }
}
