package com.tecsup.app.micro.order.infrastructure.persistence.repository;

import com.tecsup.app.micro.order.infrastructure.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {

    @Override
    @EntityGraph(attributePaths = "items")
    Optional<OrderEntity> findById(Long id);

    @EntityGraph(attributePaths = "items")
    List<OrderEntity> findByCustomerAuthUserIdOrderByCreatedAtDesc(String customerAuthUserId);
}
