package com.movieshop.server.service;

import com.movieshop.server.domain.Film;
import com.movieshop.server.model.FilmDTO;
import com.movieshop.server.model.MoviePageResponse;

import java.util.List;

public interface IFilmService {
    Film getFilmById(Integer id);

    Film getFilmByTitle(String title);

    List<Film> getAllFilms();

    MoviePageResponse findAll(Integer page, Integer limit);

    Film createFilm(FilmDTO film);

    Film updateFilm(Integer id, FilmDTO filmDTO);

    void deleteFilm(Integer id);
}
