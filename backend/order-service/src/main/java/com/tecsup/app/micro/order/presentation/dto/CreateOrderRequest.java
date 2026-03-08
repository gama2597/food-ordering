package com.tecsup.app.micro.order.presentation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {

    @NotNull(message = "El restaurante es obligatorio")
    private Long restaurantId;

    @NotEmpty(message = "El pedido debe tener al menos un item")
    @Valid
    private List<CreateOrderItemRequest> items;
}
