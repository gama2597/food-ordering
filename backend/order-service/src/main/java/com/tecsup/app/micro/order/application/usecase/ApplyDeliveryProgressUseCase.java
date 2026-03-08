package com.tecsup.app.micro.order.application.usecase;

import com.tecsup.app.micro.order.domain.exception.OrderDomainException;
import com.tecsup.app.micro.order.domain.model.OrderStatus;
import com.tecsup.app.micro.order.domain.port.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ApplyDeliveryProgressUseCase {

    private final OrderRepositoryPort orderRepository;

    public void applyAssigned(Long orderId) {
        updateStatus(orderId, OrderStatus.ASSIGNED);
    }

    public void applyStarted(Long orderId) {
        updateStatus(orderId, OrderStatus.DELIVERING);
    }

    public void applyDelivered(Long orderId) {
        updateStatus(orderId, OrderStatus.DELIVERED);
    }

    private void updateStatus(Long orderId, OrderStatus nextStatus) {
        if (orderId == null) {
            throw new OrderDomainException("El orderId es obligatorio para actualizar entrega");
        }

        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderDomainException("Pedido no encontrado para orderId=" + orderId));

        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.DELIVERED) {
            return;
        }

        int currentRank = deliveryRank(order.getStatus());
        int nextRank = deliveryRank(nextStatus);
        if (currentRank == 0 || nextRank == 0) {
            return;
        }
        if (currentRank >= nextRank) {
            return;
        }

        orderRepository.save(order.toBuilder()
                .status(nextStatus)
                .updatedAt(Instant.now())
                .build());
    }

    private int deliveryRank(OrderStatus status) {
        return switch (status) {
            case PAID -> 1;
            case ASSIGNED -> 2;
            case DELIVERING -> 3;
            case DELIVERED -> 4;
            default -> 0;
        };
    }
}
