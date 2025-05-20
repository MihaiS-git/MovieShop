package com.movieshop.server.service;

import com.movieshop.server.model.FilmRequestDTO;
import com.movieshop.server.model.FilmResponseDTO;
import com.movieshop.server.model.MoviePageResponse;

import java.util.List;

public interface IFilmService {
    FilmResponseDTO getFilmById(Integer id);

    FilmResponseDTO getFilmByTitle(String title);

    List<FilmResponseDTO> getAllFilms();

    MoviePageResponse getAllFilmsPaginated(Integer page, Integer limit);

    FilmResponseDTO createFilm(FilmRequestDTO film);

    FilmResponseDTO updateFilm(Integer id, FilmRequestDTO filmRequestDTO);

    void deleteFilm(Integer id);
}
