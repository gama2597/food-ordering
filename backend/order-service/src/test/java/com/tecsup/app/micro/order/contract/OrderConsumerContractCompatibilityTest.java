package com.tecsup.app.micro.order.contract;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tecsup.app.micro.order.infrastructure.messaging.event.DeliveryAssignedEvent;
import com.tecsup.app.micro.order.infrastructure.messaging.event.DeliveryDeliveredEvent;
import com.tecsup.app.micro.order.infrastructure.messaging.event.DeliveryStartedEvent;
import com.tecsup.app.micro.order.infrastructure.messaging.event.PaymentApprovedEvent;
import com.tecsup.app.micro.order.infrastructure.messaging.event.PaymentRejectedEvent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderConsumerContractCompatibilityTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    @Test
    void shouldDeserializePaymentApprovedWithAdditionalFields() throws Exception {
        String json = """
                {
                  "eventId": "f17f5906-f63c-4e40-b8e6-1f1a53bd4b8e",
                  "eventVersion": 1,
                  "eventType": "PAYMENT_APPROVED",
                  "occurredAt": "2026-03-08T12:10:37Z",
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
    }

    @Test
    void shouldDeserializePaymentRejectedWithAdditionalFields() throws Exception {
        String json = """
                {
                  "eventId": "d476cd1d-4af9-4976-a318-fce0f5518ad4",
                  "eventVersion": 1,
                  "eventType": "PAYMENT_REJECTED",
                  "occurredAt": "2026-03-08T12:10:37Z",
                  "orderId": 101,
                  "paymentId": 5002,
                  "customerAuthUserId": "f2f65f52-8f58-4fa4-a09d-00e4e6f18f1d",
                  "amount": 45.50,
                  "currency": "PEN",
                  "status": "REJECTED",
                  "reason": "Pago rechazado por simulacion"
                }
                """;

        PaymentRejectedEvent event = objectMapper.readValue(json, PaymentRejectedEvent.class);

        assertThat(event.orderId()).isEqualTo(101L);
    }

    @Test
    void shouldDeserializeDeliveryAssignedWithAdditionalFields() throws Exception {
        String json = """
                {
                  "eventId": "58f3739e-7ec4-47f4-b3e4-d2ca17f53cb8",
                  "eventVersion": 1,
                  "eventType": "DELIVERY_ASSIGNED",
                  "occurredAt": "2026-03-08T12:21:00Z",
                  "orderId": 101,
                  "deliveryId": 9001,
                  "status": "ASSIGNED"
                }
                """;

        DeliveryAssignedEvent event = objectMapper.readValue(json, DeliveryAssignedEvent.class);

        assertThat(event.orderId()).isEqualTo(101L);
    }

    @Test
    void shouldDeserializeDeliveryStartedWithAdditionalFields() throws Exception {
        String json = """
                {
                  "eventId": "9c53dc5c-b438-4767-8f0f-19a45fc24f2f",
                  "eventVersion": 1,
                  "eventType": "DELIVERY_STARTED",
                  "occurredAt": "2026-03-08T12:24:00Z",
                  "orderId": 101,
                  "deliveryId": 9001,
                  "status": "DELIVERING"
                }
                """;

        DeliveryStartedEvent event = objectMapper.readValue(json, DeliveryStartedEvent.class);

        assertThat(event.orderId()).isEqualTo(101L);
    }

    @Test
    void shouldDeserializeDeliveryDeliveredWithAdditionalFields() throws Exception {
        String json = """
                {
                  "eventId": "7d2ef6c4-7f7f-4545-96ff-404f1f04158d",
                  "eventVersion": 1,
                  "eventType": "DELIVERY_DELIVERED",
                  "occurredAt": "2026-03-08T12:35:00Z",
                  "orderId": 101,
                  "deliveryId": 9001,
                  "status": "DELIVERED"
                }
                """;

        DeliveryDeliveredEvent event = objectMapper.readValue(json, DeliveryDeliveredEvent.class);

        assertThat(event.orderId()).isEqualTo(101L);
    }
}
