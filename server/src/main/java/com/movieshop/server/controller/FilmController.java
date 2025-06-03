package com.movieshop.server.controller;

import com.movieshop.server.model.*;
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
    public ResponseEntity<List<FilmResponseDTO>> getAllFilms() {
        return ResponseEntity.ok(filmService.getAllFilms());
    }

    @GetMapping
    public ResponseEntity<MoviePageResponse> getAllFilmsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "16") int limit,
            @RequestParam(defaultValue = "id_asc") String orderBy,
            @RequestParam(required = false) String titleFilter,
            @RequestParam(required = false) Integer yearFilter,
            @RequestParam(required = false) String ratingFilter,
            @RequestParam(required = false) String categoryFilter) {

        return ResponseEntity.ok(filmService.getAllFilmsPaginated(page, limit, orderBy, titleFilter, yearFilter, ratingFilter, categoryFilter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilmFullResponseDTO> getFilmById(@PathVariable Integer id) {
        return ResponseEntity.ok(filmService.getFilmById(id));
    }

    @PostMapping
    public ResponseEntity<FilmResponseDTO> createFilm(@RequestBody FilmRequestDTO filmRequestDTO) {
        FilmResponseDTO createdFilm = filmService.createFilm(filmRequestDTO);
        URI location = URI.create("/api/v0/films/" + createdFilm.getId());
        return ResponseEntity.created(location).body(createdFilm);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FilmUpdateResponseDTO> updateFilm(@PathVariable Integer id, @RequestBody FilmRequestDTO filmRequestDTO) {
        return ResponseEntity.ok(filmService.updateFilm(id, filmRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFilm(@PathVariable Integer id) {
        filmService.deleteFilm(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<FilmSearchResponseDTO>> searchFilmsByTitle(@RequestParam String title) {
        return ResponseEntity.ok(filmService.searchFilmsByTitle(title));
    }

}
