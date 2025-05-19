package com.movieshop.server.controller;

import com.movieshop.server.model.FilmDTO;
import com.movieshop.server.model.MoviePageResponse;
import com.movieshop.server.service.IFilmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v0/films")
public class FilmController {

    private final IFilmService filmService;

    public FilmController(IFilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<FilmDTO>> getAllFilms() {
        return ResponseEntity.ok(filmService.getAllFilms());
    }

    @GetMapping
    public ResponseEntity<MoviePageResponse> getAllFilmsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "16") int limit) {

        return ResponseEntity.ok(filmService.getAllFilmsPaginated(page, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilmDTO> getFilmById(@PathVariable Integer id) {
        return ResponseEntity.ok(filmService.getFilmById(id));
    }

    @PostMapping
    public ResponseEntity<FilmDTO> createFilm(@RequestBody FilmDTO filmDTO) {
        FilmDTO createdFilm = filmService.createFilm(filmDTO);
        URI location = URI.create("/api/v0/films/" + createdFilm.getId());
        return ResponseEntity.created(location).body(createdFilm);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FilmDTO> updateFilm(@PathVariable Integer id, @RequestBody FilmDTO filmDTO) {
        return ResponseEntity.ok(filmService.updateFilm(id, filmDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFilm(@PathVariable Integer id) {
        filmService.deleteFilm(id);
        return ResponseEntity.noContent().build();
    }
}
