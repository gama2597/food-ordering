package com.tecsup.app.micro.order.infrastructure.messaging.publisher;

import com.tecsup.app.micro.order.domain.model.Order;
import com.tecsup.app.micro.order.domain.port.OrderEventPublisherPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NoOpOrderEventPublisherAdapter implements OrderEventPublisherPort {

    @Override
    public void publishOrderCreated(Order order) {
        log.warn("No hay publisher Kafka configurado. Evento order.created omitido para orderId={}", order.getId());
    }
}
