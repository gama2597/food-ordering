package com.tecsup.app.micro.catalog.application.service;

import com.tecsup.app.micro.catalog.domain.model.Product;
import java.util.List;

/**
 * Servicio de aplicacion que orquesta casos de uso y centraliza el flujo de negocio para la capa de presentacion.
 */
public interface ProductApplicationService {
    Product addProductToRestaurant(Product product);

    List<Product> getProductsByRestaurant(Long restaurantId);

    void deactivateProduct(Long productId);
}

