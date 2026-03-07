package com.tecsup.app.micro.catalog.infrastructure.persistence.repository;

import com.tecsup.app.micro.catalog.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
    // Custom query para buscar platos de un restaurante específico
    List<ProductEntity> findByRestaurantId(Long restaurantId);
}