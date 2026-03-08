package com.tecsup.app.micro.order.infrastructure.messaging.mapper;

import com.tecsup.app.micro.order.domain.model.Order;
import com.tecsup.app.micro.order.domain.model.OrderItem;
import com.tecsup.app.micro.order.infrastructure.messaging.event.OrderCreatedEvent;
import org.springframework.stereotype.Component;

@Component
public class OrderEventMapper {

    public OrderCreatedEvent toOrderCreatedEvent(Order order) {
        return new OrderCreatedEvent(
                order.getId(),
                order.getCustomerAuthUserId(),
                order.getRestaurantId(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                order.getItems().stream().map(this::toItemEvent).toList()
        );
    }

    private OrderCreatedEvent.OrderCreatedItemEvent toItemEvent(OrderItem item) {
        return new OrderCreatedEvent.OrderCreatedItemEvent(
                item.getProductId(),
                item.getProductName(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getSubtotal()
        );
    }
}
