package com.tecsup.app.micro.order.application.usecase;

import com.tecsup.app.micro.order.domain.exception.OrderDomainException;
import com.tecsup.app.micro.order.domain.model.Order;
import com.tecsup.app.micro.order.domain.port.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListMyOrdersUseCase {

    private final OrderRepositoryPort orderRepository;

    public List<Order> execute(String customerAuthUserId) {
        if (customerAuthUserId == null || customerAuthUserId.isBlank()) {
            throw new OrderDomainException("El usuario autenticado es obligatorio");
        }
        return orderRepository.findByCustomerAuthUserId(customerAuthUserId);
    }
}
