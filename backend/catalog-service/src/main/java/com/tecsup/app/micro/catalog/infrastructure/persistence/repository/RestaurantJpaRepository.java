package com.tecsup.app.micro.catalog.infrastructure.persistence.repository;

import com.tecsup.app.micro.catalog.infrastructure.persistence.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantJpaRepository extends JpaRepository<RestaurantEntity, Long> {
    // Custom query para traer solo los activos
    List<RestaurantEntity> findByActiveTrue();
}