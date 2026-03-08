package com.tecsup.app.micro.order.infrastructure.client.adapter;

import com.tecsup.app.micro.order.domain.exception.OrderDomainException;
import com.tecsup.app.micro.order.domain.model.CatalogProductSnapshot;
import com.tecsup.app.micro.order.domain.port.CatalogQueryPort;
import com.tecsup.app.micro.order.infrastructure.client.CatalogServiceClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CatalogQueryAdapter implements CatalogQueryPort {

    private final CatalogServiceClient catalogServiceClient;

    @Override
    public void validateRestaurantIsActive(Long restaurantId) {
        try {
            var restaurant = catalogServiceClient.getRestaurantById(restaurantId);
            if (restaurant == null) {
                throw new OrderDomainException("No se pudo validar el restaurante del pedido");
            }
            if (!restaurant.active()) {
                throw new OrderDomainException("No se puede crear el pedido: el restaurante no esta activo");
            }
        } catch (FeignException.NotFound ex) {
            throw new OrderDomainException("No se puede crear el pedido: el restaurante no existe");
        } catch (FeignException ex) {
            throw new OrderDomainException("No se pudo validar el restaurante del pedido");
        }
    }

    @Override
    public Map<Long, CatalogProductSnapshot> getProductsByRestaurant(Long restaurantId) {
        try {
            return catalogServiceClient.getProductsByRestaurant(restaurantId).stream()
                    .map(p -> CatalogProductSnapshot.builder()
                            .productId(p.id())
                            .name(p.name())
                            .price(p.price())
                            .available(p.available())
                            .build())
                    .collect(Collectors.toMap(CatalogProductSnapshot::getProductId, Function.identity()));
        } catch (FeignException ex) {
            throw new OrderDomainException("No se pudo consultar los productos del restaurante");
        }
    }
}
