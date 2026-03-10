package com.tecsup.app.micro.order.domain.port;

import com.tecsup.app.micro.order.domain.model.Order;

/**
 * Puerto de Salida: Mensajería Asíncrona.
 * Interfaz para gritar eventos al mundo exterior (Kafka).
 */
public interface OrderEventPublisherPort {

    void publishOrderCreated(Order order);

    void publishPaymentRequested(Order order);
}
