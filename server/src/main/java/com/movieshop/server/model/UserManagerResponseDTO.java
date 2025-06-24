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
public class UserManagerResponseDTO {

    private Integer id;

    private String firstName;

    private String lastName;
}
