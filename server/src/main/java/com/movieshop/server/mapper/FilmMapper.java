package com.movieshop.server.mapper;

import com.movieshop.server.domain.Category;
import com.movieshop.server.domain.Film;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.model.ActorDTO;
import com.movieshop.server.model.FilmDTO;
import com.movieshop.server.repository.ActorRepository;
import com.movieshop.server.repository.CategoryRepository;
import com.movieshop.server.repository.LanguageRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class FilmMapper {

    private final LanguageRepository languageRepository;
    private final ActorRepository actorRepository;
    private final CategoryRepository categoryRepository;

    public FilmMapper(
            LanguageRepository languageRepository,
            ActorRepository actorRepository,
            CategoryRepository categoryRepository
    ) {
        this.languageRepository = languageRepository;
        this.actorRepository = actorRepository;
        this.categoryRepository = categoryRepository;
    }

    public FilmDTO toDto(Film film) {
        if (film == null) {
            return null;
        }
        return FilmDTO.builder()
                .id(film.getId())
                .title(film.getTitle())
                .description(film.getDescription())
                .releaseYear(film.getReleaseYear())
                .language(film.getLanguage() == null ? null :
                        film.getLanguage().getName())
                .originalLanguage(film.getOriginalLanguage() == null ? null :
                        film.getOriginalLanguage().getName())
                .rentalDuration(film.getRentalDuration())
                .rentalRate(film.getRentalRate())
                .length(film.getLength())
                .replacementCost(film.getReplacementCost())
                .rating(film.getRating())
                .lastUpdate(film.getLastUpdate())
                .actors(film.getActors() == null ? null :
                        film.getActors().stream().map(actor ->
                                        ActorDTO.builder()
                                                .id(actor.getId())
                                                .firstName(actor.getFirstName())
                                                .lastName(actor.getLastName())
                                                .build())
                                .toList())
                .categories(film.getCategories() == null ? null :
                        film.getCategories().stream().map(Category::getName).toList())
                .build();
    }

    public Film toEntity(FilmDTO filmDTO) {
        if (filmDTO == null) {
            return null;
        }
        return Film.builder()
                .id(filmDTO.getId())
                .title(filmDTO.getTitle())
                .description(filmDTO.getDescription())
                .releaseYear(filmDTO.getReleaseYear())
                .language(filmDTO.getLanguage() == null ? null :
                        languageRepository.findByName(filmDTO.getLanguage()).orElseThrow(() ->
                                new EntityNotFoundException("Language not found: " + filmDTO.getLanguage())))
                .originalLanguage(filmDTO.getOriginalLanguage() == null ? null :
                        languageRepository.findByName(filmDTO.getOriginalLanguage()).orElseThrow(() ->
                                new EntityNotFoundException("Language not found: " + filmDTO.getLanguage())))
                .rentalDuration(filmDTO.getRentalDuration())
                .rentalRate(filmDTO.getRentalRate())
                .length(filmDTO.getLength())
                .replacementCost(filmDTO.getReplacementCost())
                .rating(filmDTO.getRating())
                .lastUpdate(filmDTO.getLastUpdate())
                .categories(filmDTO.getCategories() == null ? null :
                        filmDTO.getCategories().stream().map(category ->
                                        categoryRepository.findByName(category).orElseThrow(() ->
                                                new ResourceNotFoundException("Category not found: " + category)))
                                .collect(Collectors.toSet()))
                .actors(filmDTO.getActors() == null ? null :
                        filmDTO.getActors().stream().map(actor ->
                                        actorRepository.findById(actor.getId()).orElseThrow(() ->
                                                new ResourceNotFoundException("Actor not found.")))
                                .collect(Collectors.toSet()))
                .build();
    }
}
