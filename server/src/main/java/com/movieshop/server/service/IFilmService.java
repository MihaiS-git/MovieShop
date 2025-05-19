package com.movieshop.server.service;

import com.movieshop.server.domain.Film;
import com.movieshop.server.model.FilmDTO;
import com.movieshop.server.model.MoviePageResponse;

import java.util.List;

public interface IFilmService {
    FilmDTO getFilmById(Integer id);

    FilmDTO getFilmByTitle(String title);

    List<FilmDTO> getAllFilms();

    MoviePageResponse getAllFilmsPaginated(Integer page, Integer limit);

    FilmDTO createFilm(FilmDTO film);

    FilmDTO updateFilm(Integer id, FilmDTO filmDTO);

    void deleteFilm(Integer id);
}
