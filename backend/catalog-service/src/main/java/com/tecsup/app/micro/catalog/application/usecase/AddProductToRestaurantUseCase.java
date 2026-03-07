package com.tecsup.app.micro.catalog.application.usecase;

import com.tecsup.app.micro.catalog.domain.exception.CatalogDomainException;
import com.tecsup.app.micro.catalog.domain.model.Product;
import com.tecsup.app.micro.catalog.domain.port.ProductRepositoryPort;
import com.tecsup.app.micro.catalog.domain.port.RestaurantRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddProductToRestaurantUseCase {

    private final ProductRepositoryPort productRepository;
    private final RestaurantRepositoryPort restaurantRepository;

    public Product execute(Product product) {
        if (product == null) {
            throw new CatalogDomainException("El producto es obligatorio");
        }
        if (product.getRestaurantId() == null) {
            throw new CatalogDomainException("El restaurante del producto es obligatorio");
        }
        if (product.getName() == null || product.getName().isBlank()) {
            throw new CatalogDomainException("El nombre del producto es obligatorio");
        }
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new CatalogDomainException("El precio del producto debe ser mayor a cero");
        }

        log.info("Ejecutando AddProductToRestaurantUseCase para restaurante ID: {}", product.getRestaurantId());

        var restaurant = restaurantRepository.findById(product.getRestaurantId())
                .orElseThrow(
                        () -> new CatalogDomainException("No se puede crear el producto: El restaurante no existe"));
        if (!restaurant.isActive()) {
            throw new CatalogDomainException("No se puede crear el producto: El restaurante no esta activo");
        }

        Product productToSave = product.toBuilder()
                .available(true)
                .build();
        Product savedProduct = productRepository.save(productToSave);

        log.info("Producto creado satisfactoriamente con ID: {}", savedProduct.getId());
        return savedProduct;
    }
}
