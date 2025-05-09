package com.movieshop.server.service;

import com.movieshop.server.domain.Film;
import com.movieshop.server.model.MoviePageResponse;

import java.util.List;

public interface IFilmService {
    public Film getFilmById(Long id);

    List<Film> getAllFilms();

    MoviePageResponse findAll(int page, int limit);

//    public Film createFilm(Film film);
//
//    public Film updateFilm(Long id, Film film);
//
//    public void deleteFilm(Long id);
}
