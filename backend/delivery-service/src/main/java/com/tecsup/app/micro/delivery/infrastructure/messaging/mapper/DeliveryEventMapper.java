package com.tecsup.app.micro.delivery.infrastructure.messaging.mapper;

import com.tecsup.app.micro.delivery.domain.model.Delivery;
import com.tecsup.app.micro.delivery.infrastructure.messaging.event.DeliveryAssignedEvent;
import com.tecsup.app.micro.delivery.infrastructure.messaging.event.DeliveryDeliveredEvent;
import com.tecsup.app.micro.delivery.infrastructure.messaging.event.DeliveryStartedEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class DeliveryEventMapper {

    public DeliveryAssignedEvent toAssignedEvent(Delivery delivery) {
        return new DeliveryAssignedEvent(
                UUID.randomUUID().toString(),
                1,
                "DELIVERY_ASSIGNED",
                Instant.now(),
                delivery.getOrderId(),
                delivery.getId(),
                delivery.getStatus().name()
        );
    }

    public DeliveryStartedEvent toStartedEvent(Delivery delivery) {
        return new DeliveryStartedEvent(
                UUID.randomUUID().toString(),
                1,
                "DELIVERY_STARTED",
                Instant.now(),
                delivery.getOrderId(),
                delivery.getId(),
                delivery.getStatus().name()
        );
    }

    public DeliveryDeliveredEvent toDeliveredEvent(Delivery delivery) {
        return new DeliveryDeliveredEvent(
                UUID.randomUUID().toString(),
                1,
                "DELIVERY_DELIVERED",
                Instant.now(),
                delivery.getOrderId(),
                delivery.getId(),
                delivery.getStatus().name()
        );
    }
}
