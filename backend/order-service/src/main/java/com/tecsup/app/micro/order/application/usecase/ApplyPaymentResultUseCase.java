package com.tecsup.app.micro.order.application.usecase;

import com.tecsup.app.micro.order.domain.exception.OrderDomainException;
import com.tecsup.app.micro.order.domain.model.OrderStatus;
import com.tecsup.app.micro.order.domain.port.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ApplyPaymentResultUseCase {

    private final OrderRepositoryPort orderRepository;

    public void applyApproved(Long orderId) {
        if (orderId == null) {
            throw new OrderDomainException("El orderId es obligatorio para aprobar pago");
        }

        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderDomainException("Pedido no encontrado para orderId=" + orderId));

        if (order.getStatus() == OrderStatus.PAID) {
            return;
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            return;
        }
        if (order.getStatus() != OrderStatus.PAYMENT_PENDING) {
            return;
        }

        orderRepository.save(order.toBuilder()
                .status(OrderStatus.PAID)
                .updatedAt(Instant.now())
                .build());
    }

    public void applyRejected(Long orderId) {
        if (orderId == null) {
            throw new OrderDomainException("El orderId es obligatorio para rechazar pago");
        }

        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderDomainException("Pedido no encontrado para orderId=" + orderId));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            return;
        }
        if (order.getStatus() == OrderStatus.PAID) {
            return;
        }
        if (order.getStatus() != OrderStatus.PAYMENT_PENDING) {
            return;
        }

        orderRepository.save(order.toBuilder()
                .status(OrderStatus.CANCELLED)
                .updatedAt(Instant.now())
                .build());
    }
}
