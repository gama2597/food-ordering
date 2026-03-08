package com.tecsup.app.micro.user.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateMyProfileRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;

    private String phone;
}
