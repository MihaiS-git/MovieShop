package com.movieshop.server.model;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActorResponseDTO {

    private Integer id;

    private String firstName;

    private String lastName;

    private OffsetDateTime lastUpdate;

    private List<Integer> filmIds;
}
