package com.tecsup.app.micro.delivery.application.usecase;

import com.tecsup.app.micro.delivery.domain.exception.DeliveryDomainException;
import com.tecsup.app.micro.delivery.domain.model.Delivery;
import com.tecsup.app.micro.delivery.domain.port.DeliveryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetDeliveryByOrderIdUseCase {

    private final DeliveryRepositoryPort deliveryRepository;

    public Delivery execute(Long orderId) {
        if (orderId == null) {
            throw new DeliveryDomainException("El orderId es obligatorio");
        }

        return deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new DeliveryDomainException("Entrega no encontrada para pedido " + orderId));
    }
}
