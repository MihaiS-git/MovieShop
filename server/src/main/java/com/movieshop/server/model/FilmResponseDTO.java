package com.movieshop.server.model;

import com.movieshop.server.domain.Rating;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilmResponseDTO {

    private Integer id;

    private String title;

    private String description;

    private Integer releaseYear;

    private String language;

    private String originalLanguage;

    private Integer rentalDuration;

    private Double rentalRate;

    private Integer length;

    private Double replacementCost;

    private Rating rating;

    private List<String> categories;

    private List<Integer> actorIds;
}
