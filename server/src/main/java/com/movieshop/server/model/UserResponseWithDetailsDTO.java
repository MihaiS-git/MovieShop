package com.movieshop.server.model;

import java.time.OffsetDateTime;
import java.util.List;

public record UserResponseWithDetailsDTO (
    Integer id,
    String email,
    String role,
    String firstName,
    String lastName,
    AddressResponseDTO address,
    Integer storeId,
    String picture,
    List<Integer> rentalIds,
    boolean accountNonExpired,
    boolean accountNonLocked,
    boolean credentialsNonExpired,
    boolean enabled,
    OffsetDateTime createAt,
    OffsetDateTime lastUpdate,
    List<Integer> customerPaymentIds,
    List<Integer> staffPaymentIds
){}
