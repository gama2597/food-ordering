package com.tecsup.app.micro.catalog.infrastructure.persistence.repository;

import com.tecsup.app.micro.catalog.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
    // Solo productos disponibles para catálogo activo
    List<ProductEntity> findByRestaurantIdAndAvailableTrue(Long restaurantId);
}
