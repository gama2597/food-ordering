package com.tecsup.app.micro.user.domain.port;

import com.tecsup.app.micro.user.domain.model.UserProfile;

import java.util.Optional;

public interface UserProfileRepositoryPort {

    Optional<UserProfile> findByAuthUserId(String authUserId);

    UserProfile save(UserProfile userProfile);
}
