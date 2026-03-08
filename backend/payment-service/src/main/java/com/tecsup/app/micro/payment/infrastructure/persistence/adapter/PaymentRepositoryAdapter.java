package com.tecsup.app.micro.payment.infrastructure.persistence.adapter;

import com.tecsup.app.micro.payment.domain.model.Payment;
import com.tecsup.app.micro.payment.domain.port.PaymentRepositoryPort;
import com.tecsup.app.micro.payment.infrastructure.persistence.mapper.PaymentPersistenceMapper;
import com.tecsup.app.micro.payment.infrastructure.persistence.repository.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryAdapter implements PaymentRepositoryPort {

    private final PaymentJpaRepository paymentJpaRepository;
    private final PaymentPersistenceMapper mapper;

    @Override
    public Optional<Payment> findByOrderId(Long orderId) {
        return paymentJpaRepository.findByOrderId(orderId)
                .map(mapper::toDomain);
    }

    @Override
    public Payment save(Payment payment) {
        return mapper.toDomain(paymentJpaRepository.save(mapper.toEntity(payment)));
    }
}
