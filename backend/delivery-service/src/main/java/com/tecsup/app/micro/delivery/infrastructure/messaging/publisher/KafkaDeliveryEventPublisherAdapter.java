package com.tecsup.app.micro.delivery.infrastructure.messaging.publisher;

import com.tecsup.app.micro.delivery.domain.model.Delivery;
import com.tecsup.app.micro.delivery.domain.port.DeliveryEventPublisherPort;
import com.tecsup.app.micro.delivery.infrastructure.messaging.mapper.DeliveryEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Primary
@RequiredArgsConstructor
@ConditionalOnBean(KafkaTemplate.class)
public class KafkaDeliveryEventPublisherAdapter implements DeliveryEventPublisherPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final DeliveryEventMapper mapper;

    @Value("${app.kafka.topics.delivery-assigned:delivery.assigned}")
    private String deliveryAssignedTopic;

    @Value("${app.kafka.topics.delivery-started:delivery.started}")
    private String deliveryStartedTopic;

    @Value("${app.kafka.topics.delivery-delivered:delivery.delivered}")
    private String deliveryDeliveredTopic;

    @Override
    public void publishAssigned(Delivery delivery) {
        kafkaTemplate.send(deliveryAssignedTopic, String.valueOf(delivery.getOrderId()), mapper.toAssignedEvent(delivery));
        log.info("Evento delivery.assigned publicado para orderId={}", delivery.getOrderId());
    }

    @Override
    public void publishStarted(Delivery delivery) {
        kafkaTemplate.send(deliveryStartedTopic, String.valueOf(delivery.getOrderId()), mapper.toStartedEvent(delivery));
        log.info("Evento delivery.started publicado para orderId={}", delivery.getOrderId());
    }

    @Override
    public void publishDelivered(Delivery delivery) {
        kafkaTemplate.send(deliveryDeliveredTopic, String.valueOf(delivery.getOrderId()), mapper.toDeliveredEvent(delivery));
        log.info("Evento delivery.delivered publicado para orderId={}", delivery.getOrderId());
    }
}
