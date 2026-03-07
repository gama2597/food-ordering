package com.tecsup.app.micro.catalog.application.usecase;

import com.tecsup.app.micro.catalog.domain.exception.CatalogDomainException;
import com.tecsup.app.micro.catalog.domain.model.Product;
import com.tecsup.app.micro.catalog.domain.model.Restaurant;
import com.tecsup.app.micro.catalog.domain.port.ProductRepositoryPort;
import com.tecsup.app.micro.catalog.domain.port.RestaurantRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetProductsByRestaurantUseCaseTest {

    @Mock
    private ProductRepositoryPort productRepository;

    @Mock
    private RestaurantRepositoryPort restaurantRepository;

    @InjectMocks
    private GetProductsByRestaurantUseCase useCase;

    @Test
    void execute_shouldReturnProductsWhenRestaurantIsActive() {
        long restaurantId = 1L;
        Restaurant restaurant = Restaurant.builder().id(restaurantId).name("R1").active(true).build();
        Product product = Product.builder()
                .id(10L)
                .restaurantId(restaurantId)
                .name("Pizza")
                .price(new BigDecimal("19.90"))
                .available(true)
                .build();

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(productRepository.findByRestaurantId(restaurantId)).thenReturn(List.of(product));

        List<Product> result = useCase.execute(restaurantId);

        assertEquals(1, result.size());
        assertEquals(10L, result.getFirst().getId());
        verify(productRepository).findByRestaurantId(restaurantId);
    }

    @Test
    void execute_shouldThrowWhenRestaurantIdIsNull() {
        CatalogDomainException ex = assertThrows(CatalogDomainException.class, () -> useCase.execute(null));
        assertEquals("El ID del restaurante es obligatorio", ex.getMessage());
    }

    @Test
    void execute_shouldThrowWhenRestaurantIsInactive() {
        long restaurantId = 1L;
        Restaurant inactiveRestaurant = Restaurant.builder().id(restaurantId).name("R1").active(false).build();
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(inactiveRestaurant));

        CatalogDomainException ex = assertThrows(CatalogDomainException.class, () -> useCase.execute(restaurantId));
        assertEquals("El restaurante no esta activo", ex.getMessage());
    }
}
