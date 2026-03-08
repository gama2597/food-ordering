package com.tecsup.app.micro.delivery.domain.port;

import com.tecsup.app.micro.delivery.domain.model.Delivery;

public interface DeliveryEventPublisherPort {

    void publishAssigned(Delivery delivery);

    void publishStarted(Delivery delivery);

    void publishDelivered(Delivery delivery);
}
