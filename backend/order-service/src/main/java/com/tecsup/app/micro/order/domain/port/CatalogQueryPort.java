package com.tecsup.app.micro.order.domain.port;

import com.tecsup.app.micro.order.domain.model.CatalogProductSnapshot;

import java.util.Map;

/**
 * Puerto de Salida: Consultas a otros microservicios.
 * Interfaz para comunicarse síncronamente (OpenFeign) con el Catalog-Service.
 */
public interface CatalogQueryPort {

    void validateRestaurantIsActive(Long restaurantId);

    Map<Long, CatalogProductSnapshot> getProductsByRestaurant(Long restaurantId);
}
