package com.tecsup.app.micro.catalog.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
@AllArgsConstructor
public class Restaurant {
    private final Long id;
    private final String name;
    private final String description;
    private final String address;
    private final boolean active;
}
