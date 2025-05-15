package com.movieshop.server.model;

import com.movieshop.server.domain.Rating;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilmDTO {
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
    private LocalDateTime lastUpdate;
    private List<String> categories;
    private List<ActorDTO> actors;
}
