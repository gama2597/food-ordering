package com.tecsup.app.micro.user.domain.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Getter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class UserProfile {

    private final Long id;
    private final String authUserId;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String phone;
    private final boolean active;
    private final Instant createdAt;
    private final Instant updatedAt;
}
