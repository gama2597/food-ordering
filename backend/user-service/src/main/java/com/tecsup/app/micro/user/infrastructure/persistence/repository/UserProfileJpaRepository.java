package com.tecsup.app.micro.user.infrastructure.persistence.repository;

import com.tecsup.app.micro.user.infrastructure.persistence.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileJpaRepository extends JpaRepository<UserProfileEntity, Long> {

    Optional<UserProfileEntity> findByAuthUserId(String authUserId);
}
