package com.movieshop.server.controller;

import com.movieshop.server.domain.Film;
import com.movieshop.server.domain.Rating;
import com.movieshop.server.model.FilmDTO;
import com.movieshop.server.model.MoviePageResponse;
import com.movieshop.server.service.IFilmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(defaultValue = "16") int limit) {

        return ResponseEntity.ok(filmService.findAll(page, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilmDTO> getFilmById(@PathVariable Long id) {
        Film film = filmService.getFilmById(id);
        FilmDTO filmDTO = FilmDTO.builder()
                .id(film.getId())
                .title(film.getTitle())
                .description(film.getDescription())
                .releaseYear(film.getReleaseYear())
                .languageId(film.getLanguageId())
                .originalLanguageId(film.getOriginalLanguageId())
                .rentalDuration(film.getRentalDuration())
                .rentalRate(film.getRentalRate())
                .length(film.getLength())
                .replacementCost(film.getReplacementCost())
                .rating(Rating.valueOf(film.getRating().name()))
                .lastUpdate(film.getLastUpdate())
                .build();
        return ResponseEntity.ok(filmDTO);
    }
}
