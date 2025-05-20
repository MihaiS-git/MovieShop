package com.movieshop.server.service;

import com.movieshop.server.model.LanguageRequestDTO;
import com.movieshop.server.model.LanguageResponseDTO;

import java.util.List;

public interface ILanguageService {
    LanguageResponseDTO getLanguageById(Integer id);

    LanguageResponseDTO getLanguageByName(String name);

    List<LanguageResponseDTO> getAllLanguages();

    LanguageResponseDTO createLanguage(LanguageRequestDTO languageRequestDTO);

    LanguageResponseDTO updateLanguage(Integer id, LanguageRequestDTO languageRequestDTO);

    void deleteLanguage(Integer id);
}
