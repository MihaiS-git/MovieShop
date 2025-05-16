package com.movieshop.server.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(exclude = {"lastUpdate"})
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String address;

    @NotNull
    private String address2;

    private String district;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    @NotNull
    private City city;

    @Column(name= "postal_code")
    private String postalCode;

    private String phone;

    @Column(name = "last_update")
    private OffsetDateTime lastUpdate;

    @PrePersist
    @PreUpdate
    private void updateTimeStamps() {
        lastUpdate = OffsetDateTime.now();
    }

}
