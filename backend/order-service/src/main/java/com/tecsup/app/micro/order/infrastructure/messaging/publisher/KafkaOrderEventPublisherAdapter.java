package com.tecsup.app.micro.order.infrastructure.messaging.publisher;

import com.tecsup.app.micro.order.domain.model.Order;
import com.tecsup.app.micro.order.domain.port.OrderEventPublisherPort;
import com.tecsup.app.micro.order.infrastructure.messaging.mapper.OrderEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

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

    @Override
    public void publishOrderCreated(Order order) {
        try {
            var event = mapper.toOrderCreatedEvent(order);
            kafkaTemplate.send(orderCreatedTopic, String.valueOf(order.getId()), event);
            log.info("Evento order.created publicado para orderId={}", order.getId());
        } catch (Exception ex) {
            log.error("No se pudo publicar evento order.created para orderId={}", order.getId(), ex);
        }
    }
}
