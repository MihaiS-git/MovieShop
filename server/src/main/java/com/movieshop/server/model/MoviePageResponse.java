package com.movieshop.server.model;

import com.movieshop.server.domain.Film;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MoviePageResponse {
    private List<Film> movies;
    private long totalCount;
}
