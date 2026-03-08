package com.tecsup.app.micro.order.infrastructure.messaging.consumer;

import com.tecsup.app.micro.order.application.service.OrderApplicationService;
import com.tecsup.app.micro.order.infrastructure.messaging.event.DeliveryAssignedEvent;
import com.tecsup.app.micro.order.infrastructure.messaging.event.DeliveryDeliveredEvent;
import com.tecsup.app.micro.order.infrastructure.messaging.event.DeliveryStartedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryProgressConsumer {

    private final OrderApplicationService orderApplicationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${app.kafka.topics.delivery-assigned:delivery.assigned}")
    public void onDeliveryAssigned(String payload) {
        try {
            DeliveryAssignedEvent event = objectMapper.readValue(payload, DeliveryAssignedEvent.class);
            orderApplicationService.applyDeliveryAssigned(event.orderId());
            log.info("Evento delivery.assigned procesado para orderId={}", event.orderId());
        } catch (Exception ex) {
            log.error("Fallo procesando delivery.assigned payload={}", payload, ex);
        }
    }

    @KafkaListener(topics = "${app.kafka.topics.delivery-started:delivery.started}")
    public void onDeliveryStarted(String payload) {
        try {
            DeliveryStartedEvent event = objectMapper.readValue(payload, DeliveryStartedEvent.class);
            orderApplicationService.applyDeliveryStarted(event.orderId());
            log.info("Evento delivery.started procesado para orderId={}", event.orderId());
        } catch (Exception ex) {
            log.error("Fallo procesando delivery.started payload={}", payload, ex);
        }
    }

    @KafkaListener(topics = "${app.kafka.topics.delivery-delivered:delivery.delivered}")
    public void onDeliveryDelivered(String payload) {
        try {
            DeliveryDeliveredEvent event = objectMapper.readValue(payload, DeliveryDeliveredEvent.class);
            orderApplicationService.applyDeliveryDelivered(event.orderId());
            log.info("Evento delivery.delivered procesado para orderId={}", event.orderId());
        } catch (Exception ex) {
            log.error("Fallo procesando delivery.delivered payload={}", payload, ex);
        }
    }
}
