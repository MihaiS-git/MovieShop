package com.movieshop.server.model;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class AddressResponseDTO {

    private Integer id;

    private String address;

    private String address2;

    private String district;

    private CityResponseDTO city;

    private String postalCode;

    private String phone;

    private OffsetDateTime lastUpdate;
}
