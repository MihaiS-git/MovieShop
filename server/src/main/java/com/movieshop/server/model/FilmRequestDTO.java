package com.movieshop.server.model;

import com.movieshop.server.domain.Rating;
import jakarta.validation.constraints.*;
import lombok.*;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilmRequestDTO {

    @NotNull
    @Size(min = 1, max = 128)
    private String title;

    @NotNull
    @Size(min = 1, max = 300)
    private String description;

    @NotNull
    @Min(1930)
    @Max(2050)
    private Integer releaseYear;

    private String language;

    private String originalLanguage;

    @Min(1)
    private Integer rentalDuration;

    @Min(0)
    private Double rentalRate;

    @Min(1)
    private Integer length;

    @Min(0)
    private Double replacementCost;

    private Rating rating;

}
