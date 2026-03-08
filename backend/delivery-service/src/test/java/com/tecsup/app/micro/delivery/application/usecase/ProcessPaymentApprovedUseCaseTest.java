package com.tecsup.app.micro.delivery.application.usecase;

import com.tecsup.app.micro.delivery.application.usecase.command.PaymentApprovedCommand;
import com.tecsup.app.micro.delivery.domain.model.Delivery;
import com.tecsup.app.micro.delivery.domain.model.DeliveryStatus;
import com.tecsup.app.micro.delivery.domain.port.DeliveryEventPublisherPort;
import com.tecsup.app.micro.delivery.domain.port.DeliveryRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcessPaymentApprovedUseCaseTest {

    @Mock
    private DeliveryRepositoryPort deliveryRepository;

    @Mock
    private DeliveryEventPublisherPort deliveryEventPublisher;

    @InjectMocks
    private ProcessPaymentApprovedUseCase useCase;

    @Test
    void execute_shouldPublishAssignedStartedAndDelivered() {
        PaymentApprovedCommand command = PaymentApprovedCommand.builder()
                .orderId(1L)
                .customerAuthUserId("sub-1")
                .build();

        when(deliveryRepository.findByOrderId(1L)).thenReturn(Optional.empty());
        when(deliveryRepository.save(any(Delivery.class))).thenAnswer(invocation -> {
            Delivery input = invocation.getArgument(0);
            return input.toBuilder().id(99L).build();
        });

        Delivery result = useCase.execute(command);

        assertEquals(DeliveryStatus.DELIVERED, result.getStatus());
        verify(deliveryEventPublisher).publishAssigned(any(Delivery.class));
        verify(deliveryEventPublisher).publishStarted(any(Delivery.class));
        verify(deliveryEventPublisher).publishDelivered(any(Delivery.class));
    }

    @Test
    void execute_shouldReturnExistingDeliveryWhenAlreadyProcessed() {
        Delivery existing = Delivery.builder().id(10L).orderId(2L).status(DeliveryStatus.DELIVERED).build();
        when(deliveryRepository.findByOrderId(2L)).thenReturn(Optional.of(existing));

        Delivery result = useCase.execute(PaymentApprovedCommand.builder()
                .orderId(2L)
                .customerAuthUserId("sub-1")
                .build());

        assertEquals(10L, result.getId());
        verify(deliveryRepository, never()).save(any(Delivery.class));
        verify(deliveryEventPublisher, never()).publishAssigned(any(Delivery.class));
    }
}
