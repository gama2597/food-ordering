package com.tecsup.app.micro.user.presentation.dto;

import lombok.Data;

@Data
public class AddressResponse {

    private Long id;
    private Long userId;
    private String label;
    private String line1;
    private String line2;
    private String district;
    private String city;
    private String reference;
    private boolean primaryAddress;
    private boolean active;
}
