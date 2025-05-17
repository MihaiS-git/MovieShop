package com.movieshop.server.service;

import com.movieshop.server.domain.Actor;
import com.movieshop.server.domain.Category;
import com.movieshop.server.domain.Film;
import com.movieshop.server.domain.Language;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.FilmMapper;
import com.movieshop.server.model.FilmDTO;
import com.movieshop.server.model.MoviePageResponse;
import com.movieshop.server.repository.FilmRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmServiceImpl implements IFilmService {

    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;
    private final ILanguageService languageService;
    private final ICategoryService categoryService;
    private final IActorService actorService;

    public FilmServiceImpl(
            FilmRepository filmRepository,
            FilmMapper filmMapper,
            ILanguageService languageService,
            ICategoryService categoryService,
            IActorService actorService
    ) {
        this.filmRepository = filmRepository;
        this.filmMapper = filmMapper;
        this.languageService = languageService;
        this.categoryService = categoryService;
        this.actorService = actorService;
    }

    @Override
    public Film getFilmById(Integer id) {
        return getFilmByIdOrElseThrow(id);
    }

    @Override
    public Film getFilmByTitle(String title) {
        return filmRepository.findByTitle(title).orElseThrow(() ->
                new ResourceNotFoundException("Film not found with title: " + title));
    }

    @Override
    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }

    @Override
    public MoviePageResponse findAll(Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "id"));
        try {
            List<Film> films = filmRepository.findAll(pageable).getContent();
            long totalCount = filmRepository.count();

            return new MoviePageResponse(films, totalCount);
        } catch (Exception e) {
            log.error("Error fetching paginated films: {}", e.getMessage());
            throw new ResourceNotFoundException("Error fetching paginated films: " + e.getMessage());
        }
    }

    @Override
    public Film createFilm(FilmDTO filmDTO) {
        Language language = languageService.getLanguageByName(filmDTO.getLanguage());
        Language originalLanguage = languageService.getLanguageByName(filmDTO.getOriginalLanguage());
        List<Integer> categoryIds = filmDTO.getCategoryIds();
        Set<Category> categories;
        if (categoryIds != null && !categoryIds.isEmpty()) {
            categories = categoryIds.stream().map(categoryService::getCategoryById).collect(Collectors.toSet());
        } else {
            categories = new HashSet<>();
        }
        List<Integer> actorIds = filmDTO.getActorIds();
        Set<Actor> actors;
        if (actorIds != null && !actorIds.isEmpty()) {
            actors = actorIds.stream().map(actorService::getActorById).collect(Collectors.toSet());
        } else {
            actors = new HashSet<>();
        }

        Film film = filmMapper.toEntity(filmDTO, language, originalLanguage, categories, actors);
        return filmRepository.save(film);
    }

    @Override
    public Film updateFilm(Integer id, FilmDTO filmDTO) {
        Film existentFilm = getFilmByIdOrElseThrow(id);
        existentFilm.setTitle(filmDTO.getTitle());
        existentFilm.setDescription(filmDTO.getDescription());
        existentFilm.setReleaseYear(filmDTO.getReleaseYear());
        existentFilm.setLanguage(languageService.getLanguageByName(filmDTO.getLanguage()));
        existentFilm.setOriginalLanguage(languageService.getLanguageByName(filmDTO.getOriginalLanguage()));
        existentFilm.setRentalDuration(filmDTO.getRentalDuration());
        existentFilm.setRentalRate(filmDTO.getRentalRate());
        existentFilm.setLength(filmDTO.getLength());
        existentFilm.setReplacementCost(filmDTO.getReplacementCost());
        existentFilm.setRating(filmDTO.getRating());

        return filmRepository.save(existentFilm);
    }

    @Override
    public void deleteFilm(Integer id) {
        Film existentFilm = getFilmByIdOrElseThrow(id);
        filmRepository.delete(existentFilm);
    }

    private Film getFilmByIdOrElseThrow(Integer id) {
        return filmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Film not found with id: " + id));
    }
}

