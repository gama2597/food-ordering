package com.tecsup.app.micro.user.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 50)
    private String label;

    @Column(name = "line_1", nullable = false, length = 200)
    private String line1;

    @Column(name = "line_2", length = 200)
    private String line2;

    @Column(nullable = false, length = 80)
    private String district;

    @Column(nullable = false, length = 80)
    private String city;

    @Column(length = 255)
    private String reference;

    @Column(name = "is_primary", nullable = false)
    private boolean primaryAddress;

    @Column(nullable = false)
    private boolean active;
}
