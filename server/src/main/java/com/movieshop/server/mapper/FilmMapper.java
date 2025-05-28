package com.movieshop.server.mapper;

import com.movieshop.server.domain.Actor;
import com.movieshop.server.domain.Category;
import com.movieshop.server.domain.Film;
import com.movieshop.server.domain.Rating;
import com.movieshop.server.model.*;
import org.springframework.stereotype.Component;

@Component
public class FilmMapper {

    public FilmResponseDTO toResponseDto(Film film) {
        if (film == null) {
            return null;
        }
        return FilmResponseDTO.builder()
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
                .actorIds(film.getActors().stream().map(Actor::getId).toList())
                .categories(film.getCategories().stream().map(Category::getName).toList())
                .build();
    }

    public FilmUpdateResponseDTO toUpdateResponseDto(Film film) {
        if (film == null) {
            return null;
        }
        return FilmUpdateResponseDTO.builder()
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
                .build();
    }

    public FilmSearchResponseDTO toSearchResponseDto(Film film) {
        if (film == null) {
            return null;
        }
        return FilmSearchResponseDTO.builder()
                .id(film.getId())
                .title(film.getTitle())
                .description(film.getDescription())
                .releaseYear(film.getReleaseYear())
                .length(film.getLength())
                .rating(film.getRating())
                .build();
    }

    public FilmListItemDTO toListItemDto(Film film) {
        if (film == null) {
            return null;
        }
        return FilmListItemDTO.builder()
                .id(film.getId())
                .title(film.getTitle())
                .description(film.getDescription())
                .releaseYear(film.getReleaseYear())
                .language(film.getLanguage() != null ? film.getLanguage().getName() : null)
                .originalLanguage(film.getOriginalLanguage() != null ? film.getOriginalLanguage().getName() : null)
                .rentalRate(film.getRentalRate())
                .length(film.getLength())
                .rating(film.getRating() != null ? film.getRating().toString() : null)
                .categories(film.getCategories() != null
                        ? film.getCategories().stream().map(Category::getName).toList()
                        :null)
                .build();
    }

    public Film toEntity(FilmRequestDTO filmRequestDTO) {
        if (filmRequestDTO == null) {
            return null;
        }
        Film film = new Film();
        film.setTitle(filmRequestDTO.getTitle());
        film.setDescription(filmRequestDTO.getDescription());
        film.setReleaseYear(filmRequestDTO.getReleaseYear());
        film.setRentalDuration(filmRequestDTO.getRentalDuration());
        film.setRentalRate(filmRequestDTO.getRentalRate());
        film.setLength(filmRequestDTO.getLength());
        film.setReplacementCost(filmRequestDTO.getReplacementCost());
        film.setRating(filmRequestDTO.getRating());
        return film;
    }
}
