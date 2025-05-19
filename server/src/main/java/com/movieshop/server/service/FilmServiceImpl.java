package com.movieshop.server.service;

import com.movieshop.server.domain.Actor;
import com.movieshop.server.domain.Category;
import com.movieshop.server.domain.Film;
import com.movieshop.server.domain.Language;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.FilmMapper;
import com.movieshop.server.model.FilmDTO;
import com.movieshop.server.model.MoviePageResponse;
import com.movieshop.server.repository.ActorRepository;
import com.movieshop.server.repository.CategoryRepository;
import com.movieshop.server.repository.FilmRepository;
import com.movieshop.server.repository.LanguageRepository;
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
    private final LanguageRepository languageRepository;
    private final CategoryRepository categoryRepository;
    private final ActorRepository actorRepository;

    public FilmServiceImpl(
            FilmRepository filmRepository,
            FilmMapper filmMapper,
            LanguageRepository languageRepository,
            CategoryRepository categoryRepository,
            ActorRepository actorRepository
    ) {
        this.filmRepository = filmRepository;
        this.filmMapper = filmMapper;
        this.languageRepository = languageRepository;
        this.categoryRepository = categoryRepository;
        this.actorRepository = actorRepository;
    }

    @Override
    public FilmDTO getFilmById(Integer id) {
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Film not found with id: " + id));
        return filmMapper.toDto(film);
    }

    @Override
    public FilmDTO getFilmByTitle(String title) {
        Film film = filmRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Film not found with title: " + title));

        return filmMapper.toDto(film);
    }

    @Override
    public List<FilmDTO> getAllFilms() {
        List<Film> films = filmRepository.findAll();
        return films.stream().map(filmMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public MoviePageResponse getAllFilmsPaginated(Integer page, Integer limit) {
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
    public FilmDTO createFilm(FilmDTO filmDTO) {
        Language language = languageRepository.findByName(filmDTO.getLanguage()).orElseThrow(() ->
                new ResourceNotFoundException("Language not found with name: " + filmDTO.getLanguage()));
        Language originalLanguage = languageRepository.findByName(filmDTO.getOriginalLanguage()).orElseThrow(() ->
                new ResourceNotFoundException("Language not found with name: " + filmDTO.getLanguage()));

        List<Integer> categoryIds = filmDTO.getCategoryIds();
        Set<Category> categories;
        if (categoryIds != null && !categoryIds.isEmpty()) {
            categories = categoryIds.stream().map(id -> categoryRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Category not found by id: " + id)))
                    .collect(Collectors.toSet());
        } else {
            categories = new HashSet<>();
        }

        List<Integer> actorIds = filmDTO.getActorIds();
        Set<Actor> actors;
        if (actorIds != null && !actorIds.isEmpty()) {
            actors = actorIds.stream().map(actorId -> actorRepository.findById(actorId)
                            .orElseThrow(() -> new ResourceNotFoundException("Actor not found by id: " + actorId)))
                    .collect(Collectors.toSet());
        } else {
            actors = new HashSet<>();
        }

        Film film = filmMapper.toEntity(filmDTO, language, originalLanguage, categories, actors);
        Film savedFilm = filmRepository.save(film);

        return filmMapper.toDto(savedFilm);
    }

    @Override
    public FilmDTO updateFilm(Integer id, FilmDTO filmDTO) {
        Film existentFilm = filmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Film not found with id: " + id));
        existentFilm.setTitle(filmDTO.getTitle());
        existentFilm.setDescription(filmDTO.getDescription());
        existentFilm.setReleaseYear(filmDTO.getReleaseYear());
        existentFilm.setLanguage(languageRepository.findByName(filmDTO.getLanguage())
                .orElseThrow(() -> new ResourceNotFoundException("Language not found with name: " + filmDTO.getLanguage())));
        existentFilm.setOriginalLanguage(languageRepository.findByName(filmDTO.getOriginalLanguage())
                .orElseThrow(() -> new ResourceNotFoundException("Language not found with name: " + filmDTO.getLanguage())));
        existentFilm.setRentalDuration(filmDTO.getRentalDuration());
        existentFilm.setRentalRate(filmDTO.getRentalRate());
        existentFilm.setLength(filmDTO.getLength());
        existentFilm.setReplacementCost(filmDTO.getReplacementCost());
        existentFilm.setRating(filmDTO.getRating());

        List<Integer> categoryIds = filmDTO.getCategoryIds();
        if (!categoryIds.isEmpty()) {
            Set<Category> categories = categoryIds.stream().map(categoryId -> categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new ResourceNotFoundException("Category not found by id: " + categoryId)))
                    .collect(Collectors.toSet());
            categories.forEach(existentFilm::addCategory);
        }

        List<Integer> actorIds = filmDTO.getActorIds();
        if(!actorIds.isEmpty()) {
            Set<Actor> actors = actorIds.stream().map(actorId -> actorRepository.findById(actorId)
                            .orElseThrow(() -> new ResourceNotFoundException("Actor not found by id: " + actorId)))
                    .collect(Collectors.toSet());
            actors.forEach(existentFilm::addActor);
        }

        Film updatedFilm = filmRepository.save(existentFilm);

        return filmMapper.toDto(updatedFilm);
    }

    @Override
    public void deleteFilm(Integer id) {
        Film existentFilm = filmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Film not found with id: " + id));
        filmRepository.delete(existentFilm);
    }
}

