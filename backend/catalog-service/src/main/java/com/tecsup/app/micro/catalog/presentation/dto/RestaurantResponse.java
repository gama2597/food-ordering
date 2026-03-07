package com.tecsup.app.micro.catalog.presentation.dto;

import lombok.Data;

@Data
public class RestaurantResponse {
    private Long id;
    private String name;
    private String description;
    private String address;
    private boolean active;
}