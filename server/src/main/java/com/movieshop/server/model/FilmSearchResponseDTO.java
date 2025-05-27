package com.movieshop.server.model;

import com.movieshop.server.domain.Rating;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilmSearchResponseDTO {

    private Integer id;

    private String title;

    private String description;

    private Integer releaseYear;

    private Integer length;

    private Rating rating;

}
