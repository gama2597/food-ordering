package com.tecsup.app.micro.user.infrastructure.persistence.adapter;

import com.tecsup.app.micro.user.domain.model.UserProfile;
import com.tecsup.app.micro.user.domain.port.UserProfileRepositoryPort;
import com.tecsup.app.micro.user.infrastructure.persistence.mapper.UserProfilePersistenceMapper;
import com.tecsup.app.micro.user.infrastructure.persistence.repository.UserProfileJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserProfileRepositoryAdapter implements UserProfileRepositoryPort {

    private final UserProfileJpaRepository userProfileJpaRepository;
    private final UserProfilePersistenceMapper mapper;

    @Override
    public Optional<UserProfile> findByAuthUserId(String authUserId) {
        return userProfileJpaRepository.findByAuthUserId(authUserId)
                .map(mapper::toDomain);
    }

    @Override
    public UserProfile save(UserProfile userProfile) {
        return mapper.toDomain(userProfileJpaRepository.save(mapper.toEntity(userProfile)));
    }
}
