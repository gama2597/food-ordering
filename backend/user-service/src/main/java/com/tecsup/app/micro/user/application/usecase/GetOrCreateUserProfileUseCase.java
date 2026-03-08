package com.tecsup.app.micro.user.application.usecase;

import com.tecsup.app.micro.user.domain.exception.UserDomainException;
import com.tecsup.app.micro.user.domain.model.UserProfile;
import com.tecsup.app.micro.user.domain.port.UserProfileRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class GetOrCreateUserProfileUseCase {

    private final UserProfileRepositoryPort userProfileRepository;

    public UserProfile execute(String authUserId, String email, String firstName, String lastName) {
        if (authUserId == null || authUserId.isBlank()) {
            throw new UserDomainException("El identificador de autenticacion es obligatorio");
        }

        return userProfileRepository.findByAuthUserId(authUserId)
                .orElseGet(() -> userProfileRepository.save(UserProfile.builder()
                        .authUserId(authUserId)
                        .email(email)
                        .firstName(firstName)
                        .lastName(lastName)
                        .active(true)
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build()));
    }
}
