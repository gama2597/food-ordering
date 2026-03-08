package com.tecsup.app.micro.delivery.domain.port;

import com.tecsup.app.micro.delivery.domain.model.Delivery;

import java.util.Optional;

public interface DeliveryRepositoryPort {

    Optional<Delivery> findByOrderId(Long orderId);

    Delivery save(Delivery delivery);
}
