package com.movieshop.server.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private String picture;
}
