package com.tecsup.app.micro.catalog.domain.port;

import com.tecsup.app.micro.catalog.domain.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {
    Product save(Product product);

    Optional<Product> findById(Long id);

    List<Product> findByRestaurantId(Long restaurantId);
}