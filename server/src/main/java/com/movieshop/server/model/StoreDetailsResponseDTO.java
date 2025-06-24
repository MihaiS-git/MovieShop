package com.movieshop.server.model;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreDetailsResponseDTO {

    private Integer id;

    private UserManagerResponseDTO managerStaff;

    private AddressResponseDTO address;

    private OffsetDateTime lastUpdate;

}
