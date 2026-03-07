package com.tecsup.app.micro.catalog.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RestaurantRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String name;
    private String description;
    private String address;
}