package com.tecsup.app.micro.delivery.application.usecase;

import com.tecsup.app.micro.delivery.domain.exception.DeliveryDomainException;
import com.tecsup.app.micro.delivery.domain.model.Delivery;
import com.tecsup.app.micro.delivery.domain.port.DeliveryRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetDeliveryByOrderIdUseCaseTest {

    @Mock
    private DeliveryRepositoryPort deliveryRepository;

    @InjectMocks
    private GetDeliveryByOrderIdUseCase useCase;

    @Test
    void execute_shouldReturnDeliveryWhenExists() {
        Delivery delivery = Delivery.builder().id(1L).orderId(8L).build();
        when(deliveryRepository.findByOrderId(8L)).thenReturn(Optional.of(delivery));

        Delivery result = useCase.execute(8L);
        assertEquals(1L, result.getId());
    }

    @Test
    void execute_shouldFailWhenOrderIdIsNull() {
        DeliveryDomainException ex = assertThrows(DeliveryDomainException.class, () -> useCase.execute(null));
        assertEquals("El orderId es obligatorio", ex.getMessage());
    }
}
