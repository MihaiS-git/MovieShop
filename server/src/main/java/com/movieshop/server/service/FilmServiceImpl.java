package com.movieshop.server.service;

import com.movieshop.server.FilmSpecifications;
import com.movieshop.server.domain.Actor;
import com.movieshop.server.domain.Category;
import com.movieshop.server.domain.Film;
import com.movieshop.server.domain.Language;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.FilmMapper;
import com.movieshop.server.model.*;
import com.movieshop.server.repository.ActorRepository;
import com.movieshop.server.repository.CategoryRepository;
import com.movieshop.server.repository.FilmRepository;
import com.movieshop.server.repository.LanguageRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
            LanguageRepository languageRepository, CategoryRepository categoryRepository, ActorRepository actorRepository
    ) {
        this.filmRepository = filmRepository;
        this.filmMapper = filmMapper;
        this.languageRepository = languageRepository;
        this.categoryRepository = categoryRepository;
        this.actorRepository = actorRepository;
    }

    @Override
    public FilmFullResponseDTO getFilmById(Integer id) {
        Film film = filmRepository.findByIdWithAllRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Film not found with id: " + id));
        return filmMapper.toFullResponseDto(film);
    }

    @Override
    public FilmResponseDTO getFilmByTitle(String title) {
        Film film = filmRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Film not found with title: " + title));

        return filmMapper.toResponseDto(film);
    }

    @Override
    public List<FilmResponseDTO> getAllFilms() {
        List<Film> films = filmRepository.findAll();
        return films.stream().map(filmMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public MoviePageResponse getAllFilmsPaginated(Integer page, Integer limit, String orderBy,
                                                  String titleFilter, Integer yearFilter,
                                                  String ratingFilter, String categoryFilter
    ) {
        Sort sort = parseOrderBy(orderBy);

        Pageable pageable = PageRequest.of(page, limit, sort);

        Specification<Film> spec = Specification
                .where(FilmSpecifications.hasRating((ratingFilter)))
                .and(FilmSpecifications.titleContains(titleFilter))
                .and(FilmSpecifications.releaseYearEquals(yearFilter))
                .and(FilmSpecifications.inCategory(categoryFilter));

        Page<Film> filmPage = filmRepository.findAll(spec, pageable);

        List<FilmListItemDTO> filmDTOs = filmPage.getContent().stream()
                .map(filmMapper::toListItemDto)
                .toList();

        return new MoviePageResponse(filmDTOs, filmPage.getTotalElements());
    }

    private Sort parseOrderBy(String orderBy) {
        if (orderBy == null || orderBy.isBlank()) {
            return Sort.by(Sort.Direction.ASC, "id");
        }
        String[] parts = orderBy.split("_", 2);
        if (parts.length != 2) {
            log.warn("Invalid orderBy format '{}', falling back to id_asc", orderBy);
            return Sort.by(Sort.Direction.ASC, "id");
        }
        String field = parts[0];
        Sort.Direction direction;
        try {
            direction = Sort.Direction.fromString(parts[1]);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid sort direction '{}', defaulting to ASC", parts[1]);
            direction = Sort.Direction.ASC;
        }
        return Sort.by(direction, field);
    }

    @Override
    public FilmResponseDTO createFilm(FilmRequestDTO filmRequestDTO) {
        Language language = languageRepository.findByNameIgnoreCase(filmRequestDTO.getLanguage()).orElseThrow(() ->
                new ResourceNotFoundException("Language not found with name: " + filmRequestDTO.getLanguage()));
        Language originalLanguage = languageRepository.findByNameIgnoreCase(filmRequestDTO.getOriginalLanguage()).orElseThrow(() ->
                new ResourceNotFoundException("Language not found with name: " + filmRequestDTO.getLanguage()));

        Film film = filmMapper.toEntity(filmRequestDTO);
        film.setLanguage(language);
        film.setOriginalLanguage(originalLanguage);

        Film savedFilm = filmRepository.save(film);

        return filmMapper.toResponseDto(savedFilm);
    }

    @Override
    @Transactional
    public FilmUpdateResponseDTO updateFilm(Integer id, FilmRequestDTO filmRequestDTO) {
        Film existentFilm = filmRepository.findByIdWithBasicFields(id)
                .orElseThrow(() -> new ResourceNotFoundException("Film not found with id: " + id));
        existentFilm.setTitle(filmRequestDTO.getTitle());
        existentFilm.setDescription(filmRequestDTO.getDescription());
        existentFilm.setReleaseYear(filmRequestDTO.getReleaseYear());
        existentFilm.setRentalDuration(filmRequestDTO.getRentalDuration());
        existentFilm.setRentalRate(filmRequestDTO.getRentalRate());
        existentFilm.setLength(filmRequestDTO.getLength());
        existentFilm.setReplacementCost(filmRequestDTO.getReplacementCost());
        existentFilm.setRating(filmRequestDTO.getRating());

        Language language;
        Language originalLanguage;

        if (filmRequestDTO.getLanguage() != null && !filmRequestDTO.getLanguage().isBlank()) {
            language = languageRepository.findByNameIgnoreCase(filmRequestDTO.getLanguage())
                    .orElseThrow(() -> new ResourceNotFoundException("Language not found with name: " + filmRequestDTO.getLanguage()));
            existentFilm.addLanguage(language);
        }
        if (filmRequestDTO.getOriginalLanguage() != null && !filmRequestDTO.getOriginalLanguage().isBlank()) {
            originalLanguage = languageRepository.findByNameIgnoreCase(filmRequestDTO.getOriginalLanguage())
                    .orElseThrow(() -> new ResourceNotFoundException("Language not found with name: " + filmRequestDTO.getOriginalLanguage()));
            existentFilm.addOriginalLanguage(originalLanguage);
        }

        Film updatedFilm = filmRepository.save(existentFilm);

        return filmMapper.toUpdateResponseDto(updatedFilm);
    }

    @Override
    public void deleteFilm(Integer id) {
        Film existentFilm = filmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Film not found with id: " + id));
        filmRepository.delete(existentFilm);
    }

    @Override
    @Transactional
    public List<FilmSearchResponseDTO> searchFilmsByTitle(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isBlank()) {
            return new ArrayList<>();
        }
        List<Film> films = filmRepository.findAllByTitleContainingIgnoreCase(searchTerm.trim());
        return films.stream()
                .map(filmMapper::toSearchResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public FilmFullResponseDTO addCategoryToFilm(Integer id, AddCategoryToFilmRequestDTO categoryDTO) {
        Film existentFilm = filmRepository.findByIdWithAllRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Film not found with id: " + id));
        Category categoryToAdd = categoryRepository.findByNameIgnoreCase(categoryDTO.getCategory())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with name: " + categoryDTO.getCategory()));
        existentFilm.addCategory(categoryToAdd);
        Film updatedFilm = filmRepository.save(existentFilm);
        return filmMapper.toFullResponseDto(updatedFilm);
    }

    @Transactional
    @Override
    public FilmFullResponseDTO removeCategoryFromFilm(Integer id, String category) {
        Film existentFilm = filmRepository.findByIdWithAllRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Film not found with id: " + id));
        Category categoryToRemove = categoryRepository.findByNameIgnoreCase(category)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with name: " + category));
        if (existentFilm.getCategories().contains(categoryToRemove)) {
            existentFilm.removeCategory(categoryToRemove);
            Film updatedFilm = filmRepository.save(existentFilm);
            return filmMapper.toFullResponseDto(updatedFilm);
        } else {
            throw new ResourceNotFoundException("Category not found in film with id: " + id);
        }
    }

    @Transactional
    @Override
    public FilmFullResponseDTO addActorToFilm(Integer id, AddActorToFilmRequestDTO actorDTO) {
        Film existentFilm = filmRepository.findByIdWithAllRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Film not found with id: " + id));
        Actor actorToAdd = actorRepository.findById(actorDTO.getActorId())
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + actorDTO.getActorId()));
        existentFilm.addActor(actorToAdd);
        Film updatedFilm = filmRepository.save(existentFilm);
        return filmMapper.toFullResponseDto(updatedFilm);
    }

    @Transactional
    @Override
    public FilmFullResponseDTO removeActorFromFilm(Integer id, Integer actorId) {
        Film existentFilm = filmRepository.findByIdWithAllRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Film not found with id: " + id));
        Actor actorToRemove = actorRepository.findById(actorId)
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + actorId));
        if (existentFilm.getActors().contains(actorToRemove)) {
            existentFilm.removeActor(actorToRemove);
            Film updatedFilm = filmRepository.save(existentFilm);
            return filmMapper.toFullResponseDto(updatedFilm);
        } else {
            throw new ResourceNotFoundException("Actor not found in film with id: " + id);
        }
    }
}

