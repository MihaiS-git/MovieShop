package com.movieshop.server.model;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class AddressResponseDTO {

    private Integer id;

    private String address;

    private String address2;

    private String district;

    private String city;

    private String postalCode;

    private String phone;

    private OffsetDateTime lastUpdate;

    private List<Integer> staff;

    private List<Integer> stores;

    private List<Integer> customers;
}
