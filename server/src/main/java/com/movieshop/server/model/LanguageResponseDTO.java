package com.movieshop.server.model;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LanguageResponseDTO {

    private Integer id;

    private String name;

    private OffsetDateTime lastUpdate;

    public List<Integer> filmIds;

    public List<Integer> originalLanguageFilmIds;

}
