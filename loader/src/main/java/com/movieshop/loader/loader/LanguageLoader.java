package com.movieshop.loader.loader;

import com.movieshop.loader.domain.Language;
import com.movieshop.loader.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LanguageLoader {

    private final LanguageRepository languageRepository;

    public void load(){
        if(languageRepository.count() > 0){
            return;
        }

        List<Language> languages = List.of(
                new Language("English"),
                new Language("French"),
                new Language("German"),
                new Language("Italian"),
                new Language("Japanese"),
                new Language("Mandarin"),
                new Language("Spanish"),
                new Language("Unknown")
        );

        languageRepository.saveAll(languages);
    }
}
