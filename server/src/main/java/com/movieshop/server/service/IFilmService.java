package com.movieshop.server.service;

import com.movieshop.server.model.*;

import java.util.List;

public interface IFilmService {
    FilmFullResponseDTO getFilmById(Integer id);

    FilmResponseDTO getFilmByTitle(String title);

    List<FilmResponseDTO> getAllFilms();

    MoviePageResponse getAllFilmsPaginated(Integer page, Integer limit, String orderBy, String titleFilter, Integer yearFilter, String ratingFilter, String categoryFilter);

    FilmResponseDTO createFilm(FilmRequestDTO film);

    FilmUpdateResponseDTO updateFilm(Integer id, FilmRequestDTO filmRequestDTO);

    void deleteFilm(Integer id);

    List<FilmSearchResponseDTO> searchFilmsByTitle(String searchTerm);
}
