package com.tecsup.app.micro.order.application.usecase;

import com.tecsup.app.micro.order.domain.exception.OrderDomainException;
import com.tecsup.app.micro.order.domain.model.Order;
import com.tecsup.app.micro.order.domain.port.OrderRepositoryPort;
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
class GetMyOrderByIdUseCaseTest {

    @Mock
    private OrderRepositoryPort orderRepository;

    @InjectMocks
    private GetMyOrderByIdUseCase useCase;

    @Test
    void execute_shouldReturnOrderWhenOwnerMatches() {
        Order order = Order.builder().id(1L).customerAuthUserId("sub-1").build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = useCase.execute("sub-1", 1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void execute_shouldFailWhenOrderBelongsToAnotherUser() {
        Order order = Order.builder().id(1L).customerAuthUserId("other-sub").build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderDomainException ex = assertThrows(OrderDomainException.class, () -> useCase.execute("sub-1", 1L));
        assertEquals("No tienes permisos para ver este pedido", ex.getMessage());
    }
}
