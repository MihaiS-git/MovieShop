package com.movieshop.server.model;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class UserResponseDTO {

    private Integer id;

    private String email;

    private String role;

    private String firstName;

    private String lastName;

    private Integer addressId;

    private Integer storeId;

    private String picture;

    private List<Integer> rentalIds;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    private OffsetDateTime createAt;

    private OffsetDateTime lastUpdate;

}
