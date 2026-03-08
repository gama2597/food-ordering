package com.tecsup.app.micro.user.application.usecase;

import com.tecsup.app.micro.user.domain.exception.UserDomainException;
import com.tecsup.app.micro.user.domain.model.Address;
import com.tecsup.app.micro.user.domain.model.UserProfile;
import com.tecsup.app.micro.user.domain.port.AddressRepositoryPort;
import com.tecsup.app.micro.user.domain.port.UserProfileRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddAddressToMyProfileUseCaseTest {

    @Mock
    private AddressRepositoryPort addressRepository;

    @Mock
    private UserProfileRepositoryPort userProfileRepository;

    @InjectMocks
    private AddAddressToMyProfileUseCase useCase;

    @Test
    void execute_shouldSaveAddressWithUserId() {
        UserProfile user = UserProfile.builder().id(5L).authUserId("sub-1").email("test@mail.com").active(true).build();
        Address input = Address.builder().label("Casa").line1("Av. Uno 123").district("Surco").city("Lima").primaryAddress(true).build();

        when(userProfileRepository.findByAuthUserId("sub-1")).thenReturn(Optional.of(user));
        when(addressRepository.save(any(Address.class))).thenAnswer(inv -> inv.getArgument(0));

        Address result = useCase.execute("sub-1", input);

        ArgumentCaptor<Address> captor = ArgumentCaptor.forClass(Address.class);
        verify(addressRepository).save(captor.capture());
        assertEquals(5L, captor.getValue().getUserId());
        assertTrue(result.isActive());
    }

    @Test
    void execute_shouldThrowWhenLine1IsBlank() {
        Address input = Address.builder().label("Casa").line1(" ").district("Surco").city("Lima").build();

        UserDomainException ex = assertThrows(UserDomainException.class, () -> useCase.execute("sub-1", input));
        assertEquals("La direccion principal es obligatoria", ex.getMessage());
    }
}
