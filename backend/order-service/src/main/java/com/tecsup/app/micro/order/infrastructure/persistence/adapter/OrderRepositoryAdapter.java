package com.tecsup.app.micro.order.infrastructure.persistence.adapter;

import com.tecsup.app.micro.order.domain.model.Order;
import com.tecsup.app.micro.order.domain.port.OrderRepositoryPort;
import com.tecsup.app.micro.order.infrastructure.persistence.mapper.OrderPersistenceMapper;
import com.tecsup.app.micro.order.infrastructure.persistence.repository.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderPersistenceMapper mapper;

    @Override
    public Order save(Order order) {
        return mapper.toDomain(orderJpaRepository.save(mapper.toEntity(order)));
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Order> findByCustomerAuthUserId(String customerAuthUserId) {
        return mapper.toDomainList(orderJpaRepository.findByCustomerAuthUserIdOrderByCreatedAtDesc(customerAuthUserId));
    }
}
