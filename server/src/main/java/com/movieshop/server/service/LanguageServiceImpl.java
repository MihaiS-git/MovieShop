package com.movieshop.server.service;

import com.movieshop.server.domain.Language;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.LanguageMapper;
import com.movieshop.server.model.LanguageDTO;
import com.movieshop.server.repository.LanguageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageServiceImpl implements ILanguageService {

    private final LanguageRepository languageRepository;
    private final LanguageMapper languageMapper;

    public LanguageServiceImpl(LanguageRepository languageRepository, LanguageMapper languageMapper) {
        this.languageRepository = languageRepository;
        this.languageMapper = languageMapper;
    }

    @Override
    public Language getLanguageById(Integer id) {
        return getLanguageByIdOrElseThrow(id);
    }

    @Override
    public Language getLanguageByName(String name) {
        return languageRepository.findByName(name).orElseThrow(() ->
                new ResourceNotFoundException("Language not found with name: " + name));
    }

    @Override
    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }

    @Override
    public Language createLanguage(LanguageDTO languageDTO) {
        return languageRepository.save(languageMapper.toEntity(languageDTO));
    }

    @Override
    public Language updateLanguage(Integer id, LanguageDTO languageDTO) {
        Language existentLanguage = getLanguageByIdOrElseThrow(id);
        existentLanguage.setName(languageDTO.getName());
        return languageRepository.save(existentLanguage);
    }

    @Override
    public void deleteLanguage(Integer id) {
        Language existentLanguage = getLanguageByIdOrElseThrow(id);
        languageRepository.deleteById(id);
    }

    private Language getLanguageByIdOrElseThrow(Integer id) {
        return languageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Language not found with id: " + id));
    }
}
