package com.movieshop.server.service;

import com.movieshop.server.domain.Language;
import com.movieshop.server.model.LanguageDTO;

import java.util.List;

public interface ILanguageService {
    Language getLanguageById(Integer id);

    Language getLanguageByName(String name);

    List<Language> getAllLanguages();

    Language createLanguage(LanguageDTO languageDTO);

    Language updateLanguage(Integer id, LanguageDTO languageDTO);

    void deleteLanguage(Integer id);
}
