package com.movieshop.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilmListItemDTO {
    private Integer id;
    private String title;
    private String description;
    private Integer releaseYear;
    private String language;
    private String originalLanguage;
    private Double rentalRate;
    private Integer length;
    private String rating;
    private List<String> categories;
}
