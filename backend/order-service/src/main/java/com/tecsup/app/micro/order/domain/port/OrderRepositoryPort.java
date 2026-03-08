package com.tecsup.app.micro.order.domain.port;

import com.tecsup.app.micro.order.domain.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepositoryPort {

    Order save(Order order);

    Optional<Order> findById(Long id);

    List<Order> findByCustomerAuthUserId(String customerAuthUserId);
}
