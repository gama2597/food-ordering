package com.tecsup.app.micro.user.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressRequest {

    @NotBlank(message = "La etiqueta es obligatoria")
    private String label;

    @NotBlank(message = "La direccion principal es obligatoria")
    private String line1;

    private String line2;

    @NotBlank(message = "El distrito es obligatorio")
    private String district;

    @NotBlank(message = "La ciudad es obligatoria")
    private String city;

    private String reference;

    private boolean primaryAddress;
}
