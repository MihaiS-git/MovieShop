package com.movieshop.server.controller;

import com.movieshop.server.domain.Film;
import com.movieshop.server.mapper.FilmMapper;
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
    private final FilmMapper filmMapper;

    public FilmController(IFilmService filmService, FilmMapper filmMapper) {
        this.filmService = filmService;
        this.filmMapper = filmMapper;
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
    public ResponseEntity<FilmDTO> getFilmById(@PathVariable Integer id) {
        Film film = filmService.getFilmById(id);
        FilmDTO filmDTO = filmMapper.toDto(film);
        return ResponseEntity.ok(filmDTO);
    }

    @PostMapping
    public ResponseEntity<FilmDTO> createFilm(@RequestBody FilmDTO filmDTO) {
        Film film = filmService.createFilm(filmDTO);
        FilmDTO savedFilmDTO = filmMapper.toDto(film);
        return ResponseEntity.ok(savedFilmDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FilmDTO> updateFilm(@PathVariable Integer id, @RequestBody FilmDTO filmDTO) {
        Film updatedFilm = filmService.updateFilm(id, filmDTO);
        FilmDTO updatedFilmDTO = filmMapper.toDto(updatedFilm);
        return ResponseEntity.ok(updatedFilmDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFilm(@PathVariable Integer id) {
        filmService.deleteFilm(id);
        return ResponseEntity.noContent().build();
    }
}
