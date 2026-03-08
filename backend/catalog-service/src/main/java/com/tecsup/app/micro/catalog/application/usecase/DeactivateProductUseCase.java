package com.tecsup.app.micro.catalog.application.usecase;

import com.tecsup.app.micro.catalog.domain.exception.CatalogDomainException;
import com.tecsup.app.micro.catalog.domain.port.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeactivateProductUseCase {

    private final ProductRepositoryPort productRepository;

    public void execute(Long productId) {
        if (productId == null) {
            throw new CatalogDomainException("El ID del producto es obligatorio");
        }

        var product = productRepository.findById(productId)
                .orElseThrow(() -> new CatalogDomainException("Producto no encontrado con ID: " + productId));

        if (!product.isAvailable()) {
            return;
        }

        productRepository.save(product.toBuilder()
                .available(false)
                .build());
    }
}
