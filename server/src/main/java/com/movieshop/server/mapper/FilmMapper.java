package com.movieshop.server.mapper;

import com.movieshop.server.domain.Actor;
import com.movieshop.server.domain.Category;
import com.movieshop.server.domain.Film;
import com.movieshop.server.domain.Language;
import com.movieshop.server.model.FilmDTO;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class FilmMapper {

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
                .actorIds(film.getActors().stream().map(Actor::getId).toList())
                .categoryIds(film.getCategories().stream().map(Category::getId).toList())
                .build();
    }

    public Film toEntity(
            FilmDTO filmDTO,
            Language language,
            Language originalLanguage,
            Set<Category> categories,
            Set<Actor> actors
    ) {
        if (filmDTO == null) {
            return null;
        }
        Film film = new Film();
        film.setTitle(filmDTO.getTitle());
        film.setDescription(filmDTO.getDescription());
        film.setReleaseYear(filmDTO.getReleaseYear());
        film.setRentalDuration(filmDTO.getRentalDuration());
        film.setRentalRate(filmDTO.getRentalRate());
        film.setLength(filmDTO.getLength());
        film.setReplacementCost(filmDTO.getReplacementCost());
        film.setRating(filmDTO.getRating());
        film.setLanguage(language);
        film.setOriginalLanguage(originalLanguage);
        film.setCategories(categories);
        film.setActors(actors);
        return film;
    }
}
