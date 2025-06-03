package com.movieshop.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActorResponseForFilmDTO {

    private Integer id;

    private String firstName;

    private String lastName;

    private OffsetDateTime lastUpdate;
}
