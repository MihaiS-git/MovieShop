package com.movieshop.server.service;

import com.movieshop.server.domain.Language;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.LanguageMapper;
import com.movieshop.server.model.LanguageRequestDTO;
import com.movieshop.server.model.LanguageResponseDTO;
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
    public LanguageResponseDTO getLanguageById(Integer id) {
        Language language = getLanguageByIdOrElseThrow(id);
        return languageMapper.toResponseDto(language);
    }

    @Override
    public LanguageResponseDTO getLanguageByName(String name) {
        Language language = languageRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("Language not found with name: " + name));
        return languageMapper.toResponseDto(language);
    }

    @Override
    public List<LanguageResponseDTO> getAllLanguages() {
        List<Language> languages = languageRepository.findAll();
        return languages.stream().map(languageMapper::toResponseDto).toList();
    }

    @Override
    public LanguageResponseDTO createLanguage(LanguageRequestDTO languageRequestDTO) {
        Language language = languageRepository.save(languageMapper.toEntity(languageRequestDTO));
        return languageMapper.toResponseDto(language);
    }

    @Override
    public LanguageResponseDTO updateLanguage(Integer id, LanguageRequestDTO languageRequestDTO) {
        Language existentLanguage = getLanguageByIdOrElseThrow(id);
        existentLanguage.setName(languageRequestDTO.getName());
        existentLanguage = languageRepository.save(existentLanguage);
        return languageMapper.toResponseDto(existentLanguage);
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
