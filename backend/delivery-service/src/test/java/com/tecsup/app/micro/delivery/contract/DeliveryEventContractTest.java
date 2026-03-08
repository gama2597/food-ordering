package com.tecsup.app.micro.delivery.contract;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tecsup.app.micro.delivery.infrastructure.messaging.event.DeliveryAssignedEvent;
import com.tecsup.app.micro.delivery.infrastructure.messaging.event.DeliveryDeliveredEvent;
import com.tecsup.app.micro.delivery.infrastructure.messaging.event.DeliveryStartedEvent;
import com.tecsup.app.micro.delivery.infrastructure.messaging.event.PaymentApprovedEvent;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class DeliveryEventContractTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    @Test
    void shouldDeserializePaymentApprovedPayloadFromContract() throws Exception {
        String json = """
                {
                  "eventId": "f17f5906-f63c-4e40-b8e6-1f1a53bd4b8e",
                  "eventVersion": 1,
                  "eventType": "PAYMENT_APPROVED",
                  "occurredAt": "2026-03-08T12:20:35Z",
                  "orderId": 101,
                  "paymentId": 5001,
                  "customerAuthUserId": "f2f65f52-8f58-4fa4-a09d-00e4e6f18f1d",
                  "amount": 45.50,
                  "currency": "PEN",
                  "status": "APPROVED",
                  "reason": "Pago aprobado"
                }
                """;

        PaymentApprovedEvent event = objectMapper.readValue(json, PaymentApprovedEvent.class);

        assertThat(event.orderId()).isEqualTo(101L);
        assertThat(event.customerAuthUserId()).isEqualTo("f2f65f52-8f58-4fa4-a09d-00e4e6f18f1d");
    }

    @Test
    void shouldSerializeDeliveryAssignedWithRequiredFields() throws Exception {
        DeliveryAssignedEvent event = new DeliveryAssignedEvent(
                "58f3739e-7ec4-47f4-b3e4-d2ca17f53cb8",
                1,
                "DELIVERY_ASSIGNED",
                Instant.parse("2026-03-08T12:21:00Z"),
                101L,
                9001L,
                "ASSIGNED"
        );

        JsonNode root = objectMapper.readTree(objectMapper.writeValueAsString(event));

        assertThat(root.get("eventType").asText()).isEqualTo("DELIVERY_ASSIGNED");
        assertThat(root.get("eventVersion").asInt()).isEqualTo(1);
        assertThat(root.get("orderId").asLong()).isEqualTo(101L);
        assertThat(root.get("deliveryId").asLong()).isEqualTo(9001L);
        assertThat(root.get("status").asText()).isEqualTo("ASSIGNED");
    }

    @Test
    void shouldSerializeDeliveryStartedWithRequiredFields() throws Exception {
        DeliveryStartedEvent event = new DeliveryStartedEvent(
                "9c53dc5c-b438-4767-8f0f-19a45fc24f2f",
                1,
                "DELIVERY_STARTED",
                Instant.parse("2026-03-08T12:24:00Z"),
                101L,
                9001L,
                "DELIVERING"
        );

        JsonNode root = objectMapper.readTree(objectMapper.writeValueAsString(event));

        assertThat(root.get("eventType").asText()).isEqualTo("DELIVERY_STARTED");
        assertThat(root.get("eventVersion").asInt()).isEqualTo(1);
        assertThat(root.get("orderId").asLong()).isEqualTo(101L);
        assertThat(root.get("deliveryId").asLong()).isEqualTo(9001L);
        assertThat(root.get("status").asText()).isEqualTo("DELIVERING");
    }

    @Test
    void shouldSerializeDeliveryDeliveredWithRequiredFields() throws Exception {
        DeliveryDeliveredEvent event = new DeliveryDeliveredEvent(
                "7d2ef6c4-7f7f-4545-96ff-404f1f04158d",
                1,
                "DELIVERY_DELIVERED",
                Instant.parse("2026-03-08T12:35:00Z"),
                101L,
                9001L,
                "DELIVERED"
        );

        JsonNode root = objectMapper.readTree(objectMapper.writeValueAsString(event));

        assertThat(root.get("eventType").asText()).isEqualTo("DELIVERY_DELIVERED");
        assertThat(root.get("eventVersion").asInt()).isEqualTo(1);
        assertThat(root.get("orderId").asLong()).isEqualTo(101L);
        assertThat(root.get("deliveryId").asLong()).isEqualTo(9001L);
        assertThat(root.get("status").asText()).isEqualTo("DELIVERED");
    }
}
