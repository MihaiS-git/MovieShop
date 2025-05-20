package com.movieshop.server.model;

import com.movieshop.server.domain.Address;
import com.movieshop.server.domain.Role;
import com.movieshop.server.domain.Store;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDTO {

    private String email;

    private Role role;

    private String firstName;

    private String lastName;

    private Address address;

    private Store store;

    private String picture;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

}
