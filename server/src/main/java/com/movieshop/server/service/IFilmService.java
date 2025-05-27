package com.movieshop.server.service;

import com.movieshop.server.model.*;

import java.util.List;

public interface IFilmService {
    FilmResponseDTO getFilmById(Integer id);

    FilmResponseDTO getFilmByTitle(String title);

    List<FilmResponseDTO> getAllFilms();

    MoviePageResponse getAllFilmsPaginated(Integer page, Integer limit);

    FilmResponseDTO createFilm(FilmRequestDTO film);

    FilmUpdateResponseDTO updateFilm(Integer id, FilmRequestDTO filmRequestDTO);

    void deleteFilm(Integer id);

    List<FilmSearchResponseDTO> searchFilmsByTitle(String searchTerm);
}
