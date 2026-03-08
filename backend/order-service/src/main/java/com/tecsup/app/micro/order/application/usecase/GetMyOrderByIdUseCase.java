package com.tecsup.app.micro.order.application.usecase;

import com.tecsup.app.micro.order.domain.exception.OrderDomainException;
import com.tecsup.app.micro.order.domain.model.Order;
import com.tecsup.app.micro.order.domain.port.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetMyOrderByIdUseCase {

    private final OrderRepositoryPort orderRepository;

    public Order execute(String customerAuthUserId, Long orderId) {
        if (orderId == null) {
            throw new OrderDomainException("El ID del pedido es obligatorio");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderDomainException("Pedido no encontrado con ID: " + orderId));

        if (!order.getCustomerAuthUserId().equals(customerAuthUserId)) {
            throw new OrderDomainException("No tienes permisos para ver este pedido");
        }

        return order;
    }
}
