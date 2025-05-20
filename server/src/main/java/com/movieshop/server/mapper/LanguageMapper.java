package com.movieshop.server.mapper;

import com.movieshop.server.domain.Language;
import com.movieshop.server.model.LanguageRequestDTO;
import com.movieshop.server.model.LanguageResponseDTO;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class LanguageMapper {

    public LanguageResponseDTO toResponseDto(Language language) {
        if (language == null) {
            return null;
        }
        return LanguageResponseDTO.builder()
                .id(language.getId())
                .name(language.getName())
                .lastUpdate(language.getLastUpdate())
                .filmIds(language.getFilms() != null
                        ? (language.getFilms().stream()
                        .map(film -> film.getId())
                        .toList())
                        : Collections.emptyList())
                .originalLanguageFilmIds(language.getOriginalLanguageFilms() != null
                        ? (language.getOriginalLanguageFilms().stream()
                        .map(film -> film.getId())
                        .toList())
                        : Collections.emptyList())
                .build();
    }

    public Language toEntity(LanguageRequestDTO languageRequestDTO) {
        if (languageRequestDTO == null) {
            return null;
        }
        Language language = new Language();
        language.setName(languageRequestDTO.getName());

        return language;
    }
}
