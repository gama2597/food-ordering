package com.tecsup.app.micro.catalog.application.service.impl;

import com.tecsup.app.micro.catalog.application.service.ProductApplicationService;
import com.tecsup.app.micro.catalog.application.usecase.AddProductToRestaurantUseCase;
import com.tecsup.app.micro.catalog.application.usecase.DeactivateProductUseCase;
import com.tecsup.app.micro.catalog.application.usecase.GetProductsByRestaurantUseCase;
import com.tecsup.app.micro.catalog.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de aplicacion que orquesta casos de uso y centraliza el flujo de negocio para la capa de presentacion.
 */
@Service
@RequiredArgsConstructor
public class ProductApplicationServiceImpl implements ProductApplicationService {

    private final AddProductToRestaurantUseCase addProductToRestaurantUseCase;
    private final GetProductsByRestaurantUseCase getProductsByRestaurantUseCase;
    private final DeactivateProductUseCase deactivateProductUseCase;

    @Override
    @Transactional
    public Product addProductToRestaurant(Product product) {
        return addProductToRestaurantUseCase.execute(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByRestaurant(Long restaurantId) {
        return getProductsByRestaurantUseCase.execute(restaurantId);
    }

    @Override
    @Transactional
    public void deactivateProduct(Long productId) {
        deactivateProductUseCase.execute(productId);
    }
}

