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
public class FilmListItemDTO {
    private Integer id;
    private String title;
    private String description;
    private Rating rating;
}
