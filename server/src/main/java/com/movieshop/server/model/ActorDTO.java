package com.movieshop.server.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActorDTO {
    private Integer id;
    private String firstName;
    private String lastName;
}
