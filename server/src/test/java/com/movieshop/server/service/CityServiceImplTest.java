package com.movieshop.server.service;

import com.movieshop.server.domain.City;
import com.movieshop.server.domain.Country;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.CityMapper;
import com.movieshop.server.model.CityRequestDTO;
import com.movieshop.server.model.CityResponseDTO;
import com.movieshop.server.model.CountryResponseDTO;
import com.movieshop.server.repository.AddressRepository;
import com.movieshop.server.repository.CityRepository;
import com.movieshop.server.repository.CountryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CityServiceImplTest {

    @Mock private CityRepository cityRepository;
    @Mock private CityMapper cityMapper;
    @Mock private AddressRepository addressRepository;
    @Mock private CountryRepository countryRepository;

    @InjectMocks private CityServiceImpl cityService;

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
    void getAllCities_ShouldReturnList() {
        List<City> cities = List.of(new City(), new City());
        when(cityRepository.findAll()).thenReturn(cities);

        List<CityResponseDTO> result = cityService.getAllCities();

        assertEquals(2, result.size());
        verify(cityRepository).findAll();
    }

    @Test
    void getCityById_WhenFound_ShouldReturnCity() {
        Country country = new Country();
        country.setName("Germany");
        City city = new City();
        city.setName("Berlin");
        city.setCountry(country);

        CountryResponseDTO countryResponseDTO = CountryResponseDTO.builder()
                .name("Germany")
                .build();

        CityResponseDTO expectedDto = CityResponseDTO.builder()
                .name("Berlin")
                .country(countryResponseDTO)
                .build();

        when(cityRepository.findById(1)).thenReturn(Optional.of(city));
        when(cityMapper.toResponseDto(city)).thenReturn(expectedDto);

        CityResponseDTO result = cityService.getCityById(1);

        assertNotNull(result);
        assertEquals("Berlin", result.getName());
        assertEquals("Germany", result.getCountry().getName());

        verify(cityRepository).findById(1);
        verify(cityMapper).toResponseDto(city);
    }


    @Test
    void getCityById_WhenNotFound_ShouldThrow() {
        when(cityRepository.findById(1)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            cityService.getCityById(1);
        });

        assertThat(ex.getMessage()).contains("City not found with id: 1");
    }

    @Test
    void createCity_ShouldSaveAndReturnCity() {
        CityRequestDTO dto = CityRequestDTO.builder()
                .name("Berlin")
                .country("Germany")
                .build();

        Country country = new Country();
        City cityEntity = new City();
        cityEntity.setName("Berlin");

        CountryResponseDTO countryResponseDTO = CountryResponseDTO.builder()
                .name("Germany")
                .build();

        CityResponseDTO expectedDto = CityResponseDTO.builder()
                .name("Berlin")
                .country(countryResponseDTO)
                .build();

        when(countryRepository.findByName("Germany")).thenReturn(Optional.of(country));
        when(cityMapper.toEntity(dto)).thenReturn(cityEntity);
        when(cityRepository.save(cityEntity)).thenReturn(cityEntity);
        when(cityMapper.toResponseDto(cityEntity)).thenReturn(expectedDto);

        CityResponseDTO result = cityService.createCity(dto);

        assertNotNull(result);
        assertEquals("Berlin", result.getName());
        verify(countryRepository).findByName("Germany");
        verify(cityMapper).toEntity(dto);
        verify(cityRepository).save(cityEntity);
        verify(cityMapper).toResponseDto(cityEntity);
    }


    @Test
    void createCity_WhenAddressNotFound_ShouldThrow() {
        CityRequestDTO dto = CityRequestDTO.builder()
                .name("Berlin")
                .country("Germany")
                .build();

        when(addressRepository.findById(10)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cityService.createCity(dto));
    }

    @Test
    void createCity_WhenCountryNotFound_ShouldThrow() {
        CityRequestDTO dto = CityRequestDTO.builder()
                .name("Berlin")
                .country("Neverland")
                .build();

        when(countryRepository.findByName("Neverland")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cityService.createCity(dto));
    }

    @Test
    void updateCity_WhenFound_ShouldUpdateAndReturnCity() {
        CityRequestDTO dto = CityRequestDTO.builder()
                .name("UpdatedCity")
                .country("France")
                .build();

        City existingCity = new City();
        existingCity.setName("OldName");

        Country country = new Country();
        City savedCity = new City();
        savedCity.setName("UpdatedCity");
        savedCity.setCountry(country);

        CountryResponseDTO countryResponseDTO = CountryResponseDTO.builder()
                .name("France")
                .build();

        CityResponseDTO expectedDto = CityResponseDTO.builder()
                .name("UpdatedCity")
                .country(countryResponseDTO)
                .build();

        when(cityRepository.findById(1)).thenReturn(Optional.of(existingCity));
        when(countryRepository.findByName("France")).thenReturn(Optional.of(country));
        when(cityRepository.save(existingCity)).thenReturn(savedCity);
        when(cityMapper.toResponseDto(savedCity)).thenReturn(expectedDto);

        CityResponseDTO result = cityService.updateCity(1, dto);

        assertNotNull(result);
        assertEquals("UpdatedCity", result.getName());
        assertEquals("France", result.getCountry().getName());

        assertEquals("UpdatedCity", existingCity.getName());
        assertSame(country, existingCity.getCountry());

        verify(cityRepository).findById(1);
        verify(countryRepository).findByName("France");
        verify(cityRepository).save(existingCity);
        verify(cityMapper).toResponseDto(savedCity);
    }


    @Test
    void updateCity_WhenNotFound_ShouldThrow() {
        CityRequestDTO dto = new CityRequestDTO();
        when(cityRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cityService.updateCity(1, dto));
    }

    @Test
    void updateCity_WhenCountryNotFound_ShouldThrow() {
        CityRequestDTO dto = CityRequestDTO.builder().country("Mars").build();
        City existingCity = new City();

        when(cityRepository.findById(1)).thenReturn(Optional.of(existingCity));
        when(countryRepository.findByName("Mars")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cityService.updateCity(1, dto));
    }

    @Test
    void deleteCity_WhenFound_ShouldDelete() {
        City city = new City();
        when(cityRepository.findById(1)).thenReturn(Optional.of(city));

        cityService.deleteCity(1);

        verify(cityRepository).findById(1);
        verify(cityRepository).deleteById(1);
    }

    @Test
    void deleteCity_WhenNotFound_ShouldThrow() {
        when(cityRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cityService.deleteCity(1));
    }
}
