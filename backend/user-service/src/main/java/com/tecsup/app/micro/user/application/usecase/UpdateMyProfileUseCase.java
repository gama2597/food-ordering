package com.tecsup.app.micro.user.application.usecase;

import com.tecsup.app.micro.user.domain.exception.UserDomainException;
import com.tecsup.app.micro.user.domain.model.UserProfile;
import com.tecsup.app.micro.user.domain.port.UserProfileRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UpdateMyProfileUseCase {

    private final UserProfileRepositoryPort userProfileRepository;

    public UserProfile execute(String authUserId, UserProfile profile) {
        if (profile == null) {
            throw new UserDomainException("El perfil es obligatorio");
        }

        UserProfile current = userProfileRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new UserDomainException("Perfil de usuario no encontrado"));

        UserProfile updated = current.toBuilder()
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .phone(profile.getPhone())
                .updatedAt(Instant.now())
                .build();

        return userProfileRepository.save(updated);
    }
}
