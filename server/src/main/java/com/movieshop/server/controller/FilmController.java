package com.movieshop.server.controller;

import com.movieshop.server.domain.Film;
import com.movieshop.server.model.MoviePageResponse;
import com.movieshop.server.service.IFilmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v0/films")
public class FilmController {

    private final IFilmService filmService;

    public FilmController(IFilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Film>> getAllFilms() {
        List<Film> films = filmService.getAllFilms();
        return ResponseEntity.ok(films);
    }

    @GetMapping
    public ResponseEntity<MoviePageResponse> getAllFilmsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {

        return ResponseEntity.ok(filmService.findAll(page, limit));
    }
}
