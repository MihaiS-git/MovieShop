package com.movieshop.server.service;

import com.movieshop.server.domain.Film;
import com.movieshop.server.model.FilmDTO;
import com.movieshop.server.model.MoviePageResponse;

import java.util.List;

public interface IFilmService {
    Film getFilmById(Long id);

    List<Film> getAllFilms();

    MoviePageResponse findAll(int page, int limit);

    Film createFilm(FilmDTO film);

    Film updateFilm(Long id, FilmDTO filmDTO);

    void deleteFilm(Long id);
}
