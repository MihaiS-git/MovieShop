package com.movieshop.server.model;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class UserResponseItemDTO {

    private Integer id;

    private String email;

    private String role;

    private String firstName;

    private String lastName;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    private OffsetDateTime createAt;

    private OffsetDateTime lastUpdate;
}
