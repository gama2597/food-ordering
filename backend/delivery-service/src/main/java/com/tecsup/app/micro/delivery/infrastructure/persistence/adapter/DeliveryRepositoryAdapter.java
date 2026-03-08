package com.tecsup.app.micro.delivery.infrastructure.persistence.adapter;

import com.tecsup.app.micro.delivery.domain.model.Delivery;
import com.tecsup.app.micro.delivery.domain.port.DeliveryRepositoryPort;
import com.tecsup.app.micro.delivery.infrastructure.persistence.mapper.DeliveryPersistenceMapper;
import com.tecsup.app.micro.delivery.infrastructure.persistence.repository.DeliveryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DeliveryRepositoryAdapter implements DeliveryRepositoryPort {

    private final DeliveryJpaRepository deliveryJpaRepository;
    private final DeliveryPersistenceMapper mapper;

    @Override
    public Optional<Delivery> findByOrderId(Long orderId) {
        return deliveryJpaRepository.findByOrderId(orderId)
                .map(mapper::toDomain);
    }

    @Override
    public Delivery save(Delivery delivery) {
        return mapper.toDomain(deliveryJpaRepository.save(mapper.toEntity(delivery)));
    }
}
