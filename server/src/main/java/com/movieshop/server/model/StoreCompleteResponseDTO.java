package com.movieshop.server.model;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreCompleteResponseDTO {

    private Integer id;

    private UserResponseWithAddressDTO managerStaff;

    private AddressResponseDTO address;

    private OffsetDateTime lastUpdate;

}
