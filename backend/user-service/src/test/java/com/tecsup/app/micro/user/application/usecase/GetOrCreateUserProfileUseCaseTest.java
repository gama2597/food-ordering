package com.tecsup.app.micro.user.application.usecase;

import com.tecsup.app.micro.user.domain.exception.UserDomainException;
import com.tecsup.app.micro.user.domain.model.UserProfile;
import com.tecsup.app.micro.user.domain.port.UserProfileRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetOrCreateUserProfileUseCaseTest {

    @Mock
    private UserProfileRepositoryPort userProfileRepository;

    @InjectMocks
    private GetOrCreateUserProfileUseCase useCase;

    @Test
    void execute_shouldReturnExistingUserProfile() {
        UserProfile existing = UserProfile.builder().id(1L).authUserId("sub-1").email("mail@test.com").active(true).build();
        when(userProfileRepository.findByAuthUserId("sub-1")).thenReturn(Optional.of(existing));

        UserProfile result = useCase.execute("sub-1", "mail@test.com", "Ana", "Lopez");

        assertEquals(1L, result.getId());
    }

    @Test
    void execute_shouldCreateWhenNotExists() {
        when(userProfileRepository.findByAuthUserId("sub-2")).thenReturn(Optional.empty());
        when(userProfileRepository.save(any(UserProfile.class))).thenAnswer(inv -> inv.getArgument(0));

        UserProfile result = useCase.execute("sub-2", "new@test.com", "Luis", "Diaz");

        assertEquals("sub-2", result.getAuthUserId());
        verify(userProfileRepository).save(any(UserProfile.class));
    }

    @Test
    void execute_shouldThrowWhenAuthUserIdIsBlank() {
        UserDomainException ex = assertThrows(UserDomainException.class,
                () -> useCase.execute(" ", "mail@test.com", "A", "B"));
        assertEquals("El identificador de autenticacion es obligatorio", ex.getMessage());
    }
}
