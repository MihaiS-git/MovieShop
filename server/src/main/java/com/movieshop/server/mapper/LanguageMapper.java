package com.movieshop.server.mapper;

import com.movieshop.server.domain.Language;
import com.movieshop.server.model.LanguageDTO;
import org.springframework.stereotype.Component;

@Component
public class LanguageMapper {

    public LanguageDTO toDto(Language language) {
        if (language == null) {
            return null;
        }
        return LanguageDTO.builder()
                .id(language.getId())
                .name(language.getName())
                .build();
    }

    public Language toEntity(LanguageDTO languageDTO) {
        if (languageDTO == null) {
            return null;
        }
        Language language = new Language();
        language.setName(languageDTO.getName());

        return language;
    }
}
