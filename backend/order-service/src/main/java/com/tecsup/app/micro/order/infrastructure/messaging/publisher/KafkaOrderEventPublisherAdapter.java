package com.tecsup.app.micro.order.infrastructure.messaging.publisher;

import com.tecsup.app.micro.order.domain.model.Order;
import com.tecsup.app.micro.order.domain.port.OrderEventPublisherPort;
import com.tecsup.app.micro.order.infrastructure.messaging.mapper.OrderEventMapper;
import com.tecsup.app.micro.order.infrastructure.observability.CorrelationIdSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * Publicador de Eventos en Kafka.
 * Implementa el Puerto de salida definido en la capa de Dominio.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnBean(KafkaTemplate.class)
@Primary
public class KafkaOrderEventPublisherAdapter implements OrderEventPublisherPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final OrderEventMapper mapper;

    @Value("${app.kafka.topics.order-created:order.created}")
    private String orderCreatedTopic;

    @Value("${app.kafka.topics.payment-requested:payment.requested}")
    private String paymentRequestedTopic;

    @Override
    public void publishOrderCreated(Order order) {
        try {
            var event = mapper.toOrderCreatedEvent(order);
            kafkaTemplate.send(MessageBuilder
                    .withPayload(event)
                    .setHeader(KafkaHeaders.TOPIC, orderCreatedTopic)
                    .setHeader(KafkaHeaders.KEY, String.valueOf(order.getId()))
                    .setHeader(CorrelationIdSupport.CORRELATION_ID_HEADER, CorrelationIdSupport.currentOrCreate())
                    .build());
            log.info("Evento order.created publicado para orderId={}", order.getId());
        } catch (Exception ex) {
            log.error("No se pudo publicar evento order.created para orderId={}", order.getId(), ex);
        }
    }

    @Override
    public void publishPaymentRequested(Order order) {
        try {
            var event = mapper.toPaymentRequestedEvent(order);
            kafkaTemplate.send(MessageBuilder
                    .withPayload(event)
                    .setHeader(KafkaHeaders.TOPIC, paymentRequestedTopic)
                    .setHeader(KafkaHeaders.KEY, String.valueOf(order.getId()))
                    .setHeader(CorrelationIdSupport.CORRELATION_ID_HEADER, CorrelationIdSupport.currentOrCreate())
                    .build());
            log.info("Evento payment.requested publicado para orderId={}", order.getId());
        } catch (Exception ex) {
            log.error("No se pudo publicar evento payment.requested para orderId={}", order.getId(), ex);
        }
    }
}
