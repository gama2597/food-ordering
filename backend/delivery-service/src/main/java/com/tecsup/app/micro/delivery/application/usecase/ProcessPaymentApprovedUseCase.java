package com.tecsup.app.micro.delivery.application.usecase;

import com.tecsup.app.micro.delivery.application.usecase.command.PaymentApprovedCommand;
import com.tecsup.app.micro.delivery.domain.exception.DeliveryDomainException;
import com.tecsup.app.micro.delivery.domain.model.Delivery;
import com.tecsup.app.micro.delivery.domain.model.DeliveryStatus;
import com.tecsup.app.micro.delivery.domain.port.DeliveryEventPublisherPort;
import com.tecsup.app.micro.delivery.domain.port.DeliveryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ProcessPaymentApprovedUseCase {

    private final DeliveryRepositoryPort deliveryRepository;
    private final DeliveryEventPublisherPort deliveryEventPublisher;

    public Delivery execute(PaymentApprovedCommand command) {
        validate(command);

        var existing = deliveryRepository.findByOrderId(command.orderId());
        if (existing.isPresent()) {
            return existing.get();
        }

        Instant now = Instant.now();
        Delivery assigned = deliveryRepository.save(Delivery.builder()
                .id(null)
                .orderId(command.orderId())
                .customerAuthUserId(command.customerAuthUserId())
                .status(DeliveryStatus.ASSIGNED)
                .assignedAt(now)
                .startedAt(null)
                .deliveredAt(null)
                .createdAt(now)
                .updatedAt(now)
                .build());
        deliveryEventPublisher.publishAssigned(assigned);

        Delivery started = deliveryRepository.save(assigned.toBuilder()
                .status(DeliveryStatus.DELIVERING)
                .startedAt(Instant.now())
                .updatedAt(Instant.now())
                .build());
        deliveryEventPublisher.publishStarted(started);

        Delivery delivered = deliveryRepository.save(started.toBuilder()
                .status(DeliveryStatus.DELIVERED)
                .deliveredAt(Instant.now())
                .updatedAt(Instant.now())
                .build());
        deliveryEventPublisher.publishDelivered(delivered);

        return delivered;
    }

    private void validate(PaymentApprovedCommand command) {
        if (command == null) {
            throw new DeliveryDomainException("El evento payment.approved es obligatorio");
        }
        if (command.orderId() == null) {
            throw new DeliveryDomainException("El orderId es obligatorio");
        }
        if (command.customerAuthUserId() == null || command.customerAuthUserId().isBlank()) {
            throw new DeliveryDomainException("El customerAuthUserId es obligatorio");
        }
    }
}
