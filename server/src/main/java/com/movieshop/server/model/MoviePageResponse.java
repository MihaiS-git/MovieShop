package com.movieshop.server.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MoviePageResponse {
    private List<FilmResponseDTO> films;
    private long totalCount;

}
