package com.movieshop.server.service;

import com.movieshop.server.domain.Country;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.CountryMapper;
import com.movieshop.server.model.CountryDTO;
import com.movieshop.server.repository.CountryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CountryServiceImplTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CountryMapper countryMapper;

    @InjectMocks
    private CountryServiceImpl countryService;

    AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void getAllCountries_ShouldReturnListOfCountryDTOs() {
        List<Country> countries = List.of(new Country(), new Country());
        when(countryRepository.findAll()).thenReturn(countries);

        CountryDTO dto1 = new CountryDTO();
        CountryDTO dto2 = new CountryDTO();
        when(countryMapper.toDto(countries.get(0))).thenReturn(dto1);
        when(countryMapper.toDto(countries.get(1))).thenReturn(dto2);

        List<CountryDTO> result = countryService.getAllCountries();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(countryRepository).findAll();
        verify(countryMapper, times(2)).toDto(any(Country.class));
    }

    @Test
    void getCountryById_WhenFound_ShouldReturnCountryDTO() {
        Integer id = 1;
        Country country = new Country();
        CountryDTO dto = new CountryDTO();

        when(countryRepository.findById(id)).thenReturn(Optional.of(country));
        when(countryMapper.toDto(country)).thenReturn(dto);

        CountryDTO result = countryService.getCountryById(id);

        assertSame(dto, result);
        verify(countryRepository).findById(id);
        verify(countryMapper).toDto(country);
    }

    @Test
    void getCountryById_WhenNotFound_ShouldThrow() {
        Integer id = 1;
        when(countryRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            countryService.getCountryById(id);
        });

        assertThat(ex.getMessage()).contains("Country not found with id: " + id);
        verify(countryRepository).findById(id);
        verifyNoInteractions(countryMapper);
    }

    @Test
    void getCountryByName_WhenFound_ShouldReturnCountryDTO() {
        String name = "USA";
        Country country = new Country();
        CountryDTO dto = new CountryDTO();

        when(countryRepository.findByName(name)).thenReturn(Optional.of(country));
        when(countryMapper.toDto(country)).thenReturn(dto);

        CountryDTO result = countryService.getCountryByName(name);

        assertSame(dto, result);
        verify(countryRepository).findByName(name);
        verify(countryMapper).toDto(country);
    }

    @Test
    void getCountryByName_WhenNotFound_ShouldThrow() {
        String name = "Unknown";
        when(countryRepository.findByName(name)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            countryService.getCountryByName(name);
        });

        assertThat(ex.getMessage()).contains("Country not found with name: " + name);
        verify(countryRepository).findByName(name);
        verifyNoInteractions(countryMapper);
    }

    @Test
    void createCountry_ShouldSaveAndReturnDTO() {
        CountryDTO inputDto = new CountryDTO();
        Country countryEntity = new Country();
        Country savedCountry = new Country();
        CountryDTO savedDto = new CountryDTO();

        when(countryMapper.toEntity(inputDto)).thenReturn(countryEntity);
        when(countryRepository.save(countryEntity)).thenReturn(savedCountry);
        when(countryMapper.toDto(savedCountry)).thenReturn(savedDto);

        CountryDTO result = countryService.createCountry(inputDto);

        assertSame(savedDto, result);
        verify(countryMapper).toEntity(inputDto);
        verify(countryRepository).save(countryEntity);
        verify(countryMapper).toDto(savedCountry);
    }

    @Test
    void updateCountry_WhenFound_ShouldUpdateAndReturnDTO() {
        Integer id = 1;
        CountryDTO updateDto = CountryDTO.builder()
                .name("NewName")
                .build();

        Country existentCountry = new Country();
        existentCountry.setName("OldName");

        Country updatedCountry = new Country();
        CountryDTO updatedDto = new CountryDTO();

        when(countryRepository.findById(id)).thenReturn(Optional.of(existentCountry));
        when(countryRepository.save(existentCountry)).thenReturn(updatedCountry);
        when(countryMapper.toDto(updatedCountry)).thenReturn(updatedDto);

        CountryDTO result = countryService.updateCountry(id, updateDto);

        assertSame(updatedDto, result);
        assertEquals("NewName", existentCountry.getName());

        verify(countryRepository).findById(id);
        verify(countryRepository).save(existentCountry);
        verify(countryMapper).toDto(updatedCountry);
    }

    @Test
    void updateCountry_WhenNotFound_ShouldThrow() {
        Integer id = 1;
        CountryDTO updateDto = new CountryDTO();

        when(countryRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            countryService.updateCountry(id, updateDto);
        });

        assertThat(ex.getMessage()).contains("Country not found with id: " + id);
        verify(countryRepository).findById(id);
        verifyNoMoreInteractions(countryRepository);
        verifyNoInteractions(countryMapper);
    }

    @Test
    void deleteCountry_WhenFound_ShouldDelete() {
        Integer id = 1;
        Country existentCountry = new Country();

        when(countryRepository.findById(id)).thenReturn(Optional.of(existentCountry));

        countryService.deleteCountry(id);

        verify(countryRepository).findById(id);
        verify(countryRepository).deleteById(id);
    }

    @Test
    void deleteCountry_WhenNotFound_ShouldThrow() {
        Integer id = 1;
        when(countryRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            countryService.deleteCountry(id);
        });

        assertThat(ex.getMessage()).contains("Country not found with id: " + id);
        verify(countryRepository).findById(id);
        verifyNoMoreInteractions(countryRepository);
    }
}
