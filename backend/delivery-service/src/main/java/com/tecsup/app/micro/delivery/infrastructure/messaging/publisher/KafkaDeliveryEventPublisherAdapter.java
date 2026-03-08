package com.tecsup.app.micro.delivery.infrastructure.messaging.publisher;

import com.tecsup.app.micro.delivery.domain.model.Delivery;
import com.tecsup.app.micro.delivery.domain.port.DeliveryEventPublisherPort;
import com.tecsup.app.micro.delivery.infrastructure.messaging.mapper.DeliveryEventMapper;
import com.tecsup.app.micro.delivery.infrastructure.observability.CorrelationIdSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
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
        kafkaTemplate.send(MessageBuilder
                .withPayload(mapper.toAssignedEvent(delivery))
                .setHeader(KafkaHeaders.TOPIC, deliveryAssignedTopic)
                .setHeader(KafkaHeaders.KEY, String.valueOf(delivery.getOrderId()))
                .setHeader(CorrelationIdSupport.CORRELATION_ID_HEADER, CorrelationIdSupport.currentOrCreate())
                .build());
        log.info("Evento delivery.assigned publicado para orderId={}", delivery.getOrderId());
    }

    @Override
    public void publishStarted(Delivery delivery) {
        kafkaTemplate.send(MessageBuilder
                .withPayload(mapper.toStartedEvent(delivery))
                .setHeader(KafkaHeaders.TOPIC, deliveryStartedTopic)
                .setHeader(KafkaHeaders.KEY, String.valueOf(delivery.getOrderId()))
                .setHeader(CorrelationIdSupport.CORRELATION_ID_HEADER, CorrelationIdSupport.currentOrCreate())
                .build());
        log.info("Evento delivery.started publicado para orderId={}", delivery.getOrderId());
    }

    @Override
    public void publishDelivered(Delivery delivery) {
        kafkaTemplate.send(MessageBuilder
                .withPayload(mapper.toDeliveredEvent(delivery))
                .setHeader(KafkaHeaders.TOPIC, deliveryDeliveredTopic)
                .setHeader(KafkaHeaders.KEY, String.valueOf(delivery.getOrderId()))
                .setHeader(CorrelationIdSupport.CORRELATION_ID_HEADER, CorrelationIdSupport.currentOrCreate())
                .build());
        log.info("Evento delivery.delivered publicado para orderId={}", delivery.getOrderId());
    }
}
