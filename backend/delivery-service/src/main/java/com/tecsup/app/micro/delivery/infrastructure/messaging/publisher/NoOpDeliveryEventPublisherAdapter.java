package com.tecsup.app.micro.delivery.infrastructure.messaging.publisher;

import com.tecsup.app.micro.delivery.domain.model.Delivery;
import com.tecsup.app.micro.delivery.domain.port.DeliveryEventPublisherPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnMissingBean(DeliveryEventPublisherPort.class)
public class NoOpDeliveryEventPublisherAdapter implements DeliveryEventPublisherPort {

    @Override
    public void publishAssigned(Delivery delivery) {
        log.warn("KafkaTemplate no disponible. No se publica delivery.assigned para orderId={}", delivery.getOrderId());
    }

    @Override
    public void publishStarted(Delivery delivery) {
        log.warn("KafkaTemplate no disponible. No se publica delivery.started para orderId={}", delivery.getOrderId());
    }

    @Override
    public void publishDelivered(Delivery delivery) {
        log.warn("KafkaTemplate no disponible. No se publica delivery.delivered para orderId={}", delivery.getOrderId());
    }
}
