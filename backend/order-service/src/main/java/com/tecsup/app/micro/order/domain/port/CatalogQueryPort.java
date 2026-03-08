package com.tecsup.app.micro.order.domain.port;

import com.tecsup.app.micro.order.domain.model.CatalogProductSnapshot;

import java.util.Map;

public interface CatalogQueryPort {

    void validateRestaurantIsActive(Long restaurantId);

    Map<Long, CatalogProductSnapshot> getProductsByRestaurant(Long restaurantId);
}
