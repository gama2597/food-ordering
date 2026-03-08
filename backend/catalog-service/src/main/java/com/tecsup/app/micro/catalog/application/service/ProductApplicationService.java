package com.tecsup.app.micro.catalog.application.service;

import com.tecsup.app.micro.catalog.domain.model.Product;
import java.util.List;

public interface ProductApplicationService {
    Product addProductToRestaurant(Product product);

    List<Product> getProductsByRestaurant(Long restaurantId);

    void deactivateProduct(Long productId);
}
