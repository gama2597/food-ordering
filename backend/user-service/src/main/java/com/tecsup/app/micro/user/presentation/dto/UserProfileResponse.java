package com.tecsup.app.micro.user.presentation.dto;

import lombok.Data;

@Data
public class UserProfileResponse {

    private Long id;
    private String authUserId;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private boolean active;
}
