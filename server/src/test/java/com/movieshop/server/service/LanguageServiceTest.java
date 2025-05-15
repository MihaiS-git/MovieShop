package com.movieshop.server.service;

import com.movieshop.server.domain.Language;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.LanguageMapper;
import com.movieshop.server.model.LanguageDTO;
import com.movieshop.server.repository.LanguageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LanguageServiceTest {
    @Mock
    private LanguageRepository languageRepository;

    @Mock
    private LanguageMapper languageMapper;

    @InjectMocks
    private LanguageServiceImpl languageService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void teatDown() throws Exception {
        closeable.close();
    }

    @Test
    void getLanguageById_whenExists_returnsLanguage() {
        Language lang = Language.builder().id(1).name("English").build();
        when(languageRepository.findById(1)).thenReturn(Optional.of(lang));

        Language result = languageService.getLanguageById(1);

        assertEquals("English", result.getName());
    }

    @Test
    void getLanguageById_whenNotFound_throwsException() {
        when(languageRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> languageService.getLanguageById(99));
    }

    @Test
    void getLanguageByName_whenExists_returnsLanguage() {
        Language lang = Language.builder().id(2).name("Spanish").build();
        when(languageRepository.findByName("Spanish")).thenReturn(Optional.of(lang));

        Language result = languageService.getLanguageByName("Spanish");

        assertEquals(2, result.getId());
    }

    @Test
    void getLanguageByName_whenNotFound_throwsException() {
        when(languageRepository.findByName("Klingon")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> languageService.getLanguageByName("Klingon"));
    }

    @Test
    void getAllLanguages_returnsList() {
        List<Language> mockList = Arrays.asList(
                Language.builder().id(1).name("English").build(),
                Language.builder().id(2).name("German").build()
        );
        when(languageRepository.findAll()).thenReturn(mockList);

        List<Language> result = languageService.getAllLanguages();

        assertEquals(2, result.size());
    }

    @Test
    void createLanguage_savesAndReturnsLanguage() {
        LanguageDTO dto = new LanguageDTO("French");
        Language entity = Language.builder().id(3).name("French").build();

        when(languageMapper.toEntity(dto)).thenReturn(entity);
        when(languageRepository.save(entity)).thenReturn(entity);

        Language result = languageService.createLanguage(dto);

        assertEquals("French", result.getName());
        verify(languageRepository).save(entity);
    }

    @Test
    void updateLanguage_whenExists_updatesAndReturnsLanguage() {
        Language existing = Language.builder().id(1).name("OldName").build();
        LanguageDTO dto = new LanguageDTO("NewName");

        when(languageRepository.findById(1)).thenReturn(Optional.of(existing));
        when(languageRepository.save(existing)).thenReturn(existing);

        Language result = languageService.updateLanguage(1, dto);

        assertEquals("NewName", result.getName());
        verify(languageRepository).save(existing);
    }

    @Test
    void updateLanguage_whenNotFound_throwsException() {
        LanguageDTO dto = new LanguageDTO("DoesNotMatter");
        when(languageRepository.findById(404)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> languageService.updateLanguage(404, dto));
    }

    @Test
    void deleteLanguage_whenExists_deletesSuccessfully() {
        Language existing = Language.builder().id(1).name("ToDelete").build();
        when(languageRepository.findById(1)).thenReturn(Optional.of(existing));

        languageService.deleteLanguage(1);

        verify(languageRepository).deleteById(1);
    }

    @Test
    void deleteLanguage_whenNotFound_throwsException() {
        when(languageRepository.findById(123)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> languageService.deleteLanguage(123));
    }
}
