package com.movieshop.server.service;

import com.movieshop.server.domain.Language;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.LanguageMapper;
import com.movieshop.server.model.LanguageRequestDTO;
import com.movieshop.server.model.LanguageResponseDTO;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void getLanguageById_whenExists_returnsLanguage() {
        Language lang = Language.builder().id(1).name("English").build();
        LanguageResponseDTO dto = LanguageResponseDTO.builder()
                .id(1)
                .name("English")
                .build();

        when(languageRepository.findById(1)).thenReturn(Optional.of(lang));
        when(languageMapper.toResponseDto(lang)).thenReturn(dto);

        LanguageResponseDTO result = languageService.getLanguageById(1);

        assertEquals("English", result.getName());
        assertEquals(1, result.getId());
    }

    @Test
    void getLanguageById_whenNotFound_throwsException() {
        when(languageRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> languageService.getLanguageById(99));
    }

    @Test
    void getLanguageByName_whenExists_returnsLanguage() {
        Language lang = Language.builder().id(2).name("Spanish").build();
        LanguageResponseDTO dto = LanguageResponseDTO.builder()
                .id(2)
                .name("Spanish")
                .build();

        when(languageRepository.findByNameIgnoreCase("Spanish")).thenReturn(Optional.of(lang));
        when(languageMapper.toResponseDto(lang)).thenReturn(dto);

        LanguageResponseDTO result = languageService.getLanguageByName("Spanish");

        assertEquals("Spanish", result.getName());
        assertEquals(2, result.getId());
    }

    @Test
    void getLanguageByName_whenNotFound_throwsException() {
        when(languageRepository.findByNameIgnoreCase("Klingon")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> languageService.getLanguageByName("Klingon"));
    }

    @Test
    void getAllLanguages_returnsList() {
        List<Language> entities = Arrays.asList(
                Language.builder().id(1).name("English").build(),
                Language.builder().id(2).name("German").build()
        );
        LanguageResponseDTO dto1 = LanguageResponseDTO.builder().id(1).name("English").build();
        LanguageResponseDTO dto2 = LanguageResponseDTO.builder().id(2).name("German").build();

        List<LanguageResponseDTO> dtos = Arrays.asList(dto1, dto2);

        when(languageRepository.findAll()).thenReturn(entities);
        when(languageMapper.toResponseDto(entities.get(0))).thenReturn(dtos.get(0));
        when(languageMapper.toResponseDto(entities.get(1))).thenReturn(dtos.get(1));

        List<LanguageResponseDTO> result = languageService.getAllLanguages();

        assertEquals(2, result.size());
        assertEquals("English", result.get(0).getName());
        assertEquals("German", result.get(1).getName());
    }

    @Test
    void createLanguage_savesAndReturnsLanguage() {
        LanguageRequestDTO requestDto = new LanguageRequestDTO("French");
        Language entity = Language.builder().id(3).name("French").build();
        LanguageResponseDTO responseDto = LanguageResponseDTO.builder()
                .id(3)
                .name("French")
                .build();

        when(languageMapper.toEntity(requestDto)).thenReturn(entity);
        when(languageRepository.save(entity)).thenReturn(entity);
        when(languageMapper.toResponseDto(entity)).thenReturn(responseDto);

        LanguageResponseDTO result = languageService.createLanguage(requestDto);

        assertEquals("French", result.getName());
        assertEquals(3, result.getId());
        verify(languageRepository).save(entity);
    }

    @Test
    void updateLanguage_whenExists_updatesAndReturnsLanguage() {
        Language existing = Language.builder().id(1).name("OldName").build();
        LanguageRequestDTO requestDto = new LanguageRequestDTO("NewName");
        LanguageResponseDTO responseDto = LanguageResponseDTO.builder()
                .id(1)
                .name("NewName")
                .build();

        when(languageRepository.findById(1)).thenReturn(Optional.of(existing));
        when(languageRepository.save(existing)).thenReturn(existing);
        when(languageMapper.toResponseDto(existing)).thenReturn(responseDto);

        LanguageResponseDTO result = languageService.updateLanguage(1, requestDto);

        assertEquals("NewName", result.getName());
        assertEquals(1, result.getId());
        verify(languageRepository).save(existing);
    }

    @Test
    void updateLanguage_whenNotFound_throwsException() {
        LanguageRequestDTO dto = new LanguageRequestDTO("DoesNotMatter");
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
