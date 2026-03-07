package com.tecsup.app.micro.catalog.application.usecase;

import com.tecsup.app.micro.catalog.domain.exception.CatalogDomainException;
import com.tecsup.app.micro.catalog.domain.model.Product;
import com.tecsup.app.micro.catalog.domain.model.Restaurant;
import com.tecsup.app.micro.catalog.domain.port.ProductRepositoryPort;
import com.tecsup.app.micro.catalog.domain.port.RestaurantRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddProductToRestaurantUseCaseTest {

    @Mock
    private ProductRepositoryPort productRepository;

    @Mock
    private RestaurantRepositoryPort restaurantRepository;

    @InjectMocks
    private AddProductToRestaurantUseCase useCase;

    @Test
    void execute_shouldSetProductAsAvailableAndPersist() {
        Product input = Product.builder()
                .restaurantId(1L)
                .name("Pizza Margarita")
                .description("Clasica")
                .price(new BigDecimal("25.90"))
                .available(false)
                .build();

        Restaurant activeRestaurant = Restaurant.builder().id(1L).name("R1").active(true).build();
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(activeRestaurant));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product toSave = invocation.getArgument(0);
            return toSave.toBuilder().id(10L).build();
        });

        Product result = useCase.execute(input);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());
        Product persisted = captor.getValue();

        assertTrue(persisted.isAvailable());
        assertEquals(10L, result.getId());
        assertTrue(result.isAvailable());
    }

    @Test
    void execute_shouldThrowWhenPriceIsInvalid() {
        Product input = Product.builder()
                .restaurantId(1L)
                .name("Pizza")
                .price(BigDecimal.ZERO)
                .build();

        CatalogDomainException ex = assertThrows(CatalogDomainException.class, () -> useCase.execute(input));
        assertEquals("El precio del producto debe ser mayor a cero", ex.getMessage());
        verifyNoInteractions(restaurantRepository, productRepository);
    }

    @Test
    void execute_shouldThrowWhenRestaurantIsInactive() {
        Product input = Product.builder()
                .restaurantId(1L)
                .name("Pizza")
                .price(new BigDecimal("10.00"))
                .build();

        Restaurant inactiveRestaurant = Restaurant.builder().id(1L).name("R1").active(false).build();
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(inactiveRestaurant));

        CatalogDomainException ex = assertThrows(CatalogDomainException.class, () -> useCase.execute(input));
        assertEquals("No se puede crear el producto: El restaurante no esta activo", ex.getMessage());
    }
}
