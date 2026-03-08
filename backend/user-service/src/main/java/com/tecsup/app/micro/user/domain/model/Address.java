package com.tecsup.app.micro.user.domain.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class Address {

    private final Long id;
    private final Long userId;
    private final String label;
    private final String line1;
    private final String line2;
    private final String district;
    private final String city;
    private final String reference;
    private final boolean primaryAddress;
    private final boolean active;
}
