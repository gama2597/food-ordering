package com.tecsup.app.micro.catalog.application.usecase;

import com.tecsup.app.micro.catalog.domain.exception.CatalogDomainException;
import com.tecsup.app.micro.catalog.domain.model.Product;
import com.tecsup.app.micro.catalog.domain.port.ProductRepositoryPort;
import com.tecsup.app.micro.catalog.domain.port.RestaurantRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetProductsByRestaurantUseCase {

    private final ProductRepositoryPort productRepository;
    private final RestaurantRepositoryPort restaurantRepository;

    public List<Product> execute(Long restaurantId) {
        if (restaurantId == null) {
            throw new CatalogDomainException("El ID del restaurante es obligatorio");
        }

        var restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CatalogDomainException("Restaurante no encontrado con ID: " + restaurantId));
        if (!restaurant.isActive()) {
            throw new CatalogDomainException("El restaurante no esta activo");
        }

        return productRepository.findByRestaurantId(restaurantId);
    }
}
