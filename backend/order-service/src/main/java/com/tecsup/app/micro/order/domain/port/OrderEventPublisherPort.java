package com.tecsup.app.micro.order.domain.port;

import com.tecsup.app.micro.order.domain.model.Order;

public interface OrderEventPublisherPort {

    void publishOrderCreated(Order order);

    void publishPaymentRequested(Order order);
}
