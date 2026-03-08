package com.tecsup.app.micro.catalog.application.usecase;

import com.tecsup.app.micro.catalog.domain.exception.CatalogDomainException;
import com.tecsup.app.micro.catalog.domain.port.RestaurantRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeactivateRestaurantUseCase {

    private final RestaurantRepositoryPort restaurantRepository;

    public void execute(Long restaurantId) {
        if (restaurantId == null) {
            throw new CatalogDomainException("El ID del restaurante es obligatorio");
        }

        var restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CatalogDomainException("Restaurante no encontrado con ID: " + restaurantId));

        if (!restaurant.isActive()) {
            return;
        }

        restaurantRepository.save(restaurant.toBuilder()
                .active(false)
                .build());
    }
}
