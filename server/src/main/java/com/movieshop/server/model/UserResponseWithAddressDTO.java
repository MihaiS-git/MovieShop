package com.movieshop.server.model;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class UserResponseWithAddressDTO {

    private Integer id;

    private String email;

    private String role;

    private String firstName;

    private String lastName;

    private AddressResponseDTO address;

    private Integer storeId;

    private String picture;

    private List<Integer> rentalIds;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    private OffsetDateTime createAt;

    private OffsetDateTime lastUpdate;

    private List<Integer> customerPaymentIds;

    private List<Integer> staffPaymentIds;
}
