package com.tecsup.app.micro.catalog.application.usecase;

import com.tecsup.app.micro.catalog.domain.exception.CatalogDomainException;
import com.tecsup.app.micro.catalog.domain.model.Restaurant;
import com.tecsup.app.micro.catalog.domain.port.RestaurantRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetRestaurantByIdUseCase {

    private final RestaurantRepositoryPort restaurantRepository;

    public Restaurant execute(Long id) {
        if (id == null) {
            throw new CatalogDomainException("El ID del restaurante es obligatorio");
        }

        return restaurantRepository.findById(id)
                .orElseThrow(() -> new CatalogDomainException("Restaurante no encontrado con ID: " + id));
    }
}
