package com.tecsup.app.micro.catalog.application.usecase;

import com.tecsup.app.micro.catalog.domain.exception.CatalogDomainException;
import com.tecsup.app.micro.catalog.domain.model.Restaurant;
import com.tecsup.app.micro.catalog.domain.port.RestaurantRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateRestaurantUseCase {

    private final RestaurantRepositoryPort restaurantRepository;

    public Restaurant execute(Restaurant restaurant) {
        if (restaurant == null) {
            throw new CatalogDomainException("El restaurante es obligatorio");
        }
        if (restaurant.getName() == null || restaurant.getName().isBlank()) {
            throw new CatalogDomainException("El nombre del restaurante es obligatorio");
        }

        log.info("Ejecutando CreateRestaurantUseCase para el restaurante: {}", restaurant.getName());

        Restaurant restaurantToSave = restaurant.toBuilder()
                .active(true)
                .build();

        Restaurant savedRestaurant = restaurantRepository.save(restaurantToSave);
        log.info("Restaurante creado satisfactoriamente con ID: {}", savedRestaurant.getId());

        return savedRestaurant;
    }
}
