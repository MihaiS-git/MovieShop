package com.movieshop.server.service;

import com.movieshop.server.domain.Address;
import com.movieshop.server.domain.City;
import com.movieshop.server.domain.Country;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.CityMapper;
import com.movieshop.server.model.CityDTO;
import com.movieshop.server.repository.AddressRepository;
import com.movieshop.server.repository.CityRepository;
import com.movieshop.server.repository.CountryRepository;
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

    @Test
    void getAllCities_ShouldReturnList() {
        List<City> cities = List.of(new City(), new City());
        when(cityRepository.findAll()).thenReturn(cities);

        List<City> result = cityService.getAllCities();

        assertEquals(2, result.size());
        verify(cityRepository).findAll();
    }

    @Test
    void getCityById_WhenFound_ShouldReturnCity() {
        City city = new City();
        when(cityRepository.findById(1)).thenReturn(Optional.of(city));

        City result = cityService.getCityById(1);

        assertSame(city, result);
        verify(cityRepository).findById(1);
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
    void getCityByName_WhenFound_ShouldReturnCity() {
        City city = new City();
        when(cityRepository.findByName("Paris")).thenReturn(Optional.of(city));

        City result = cityService.getCityByName("Paris");

        assertSame(city, result);
        verify(cityRepository).findByName("Paris");
    }

    @Test
    void getCityByName_WhenNotFound_ShouldThrow() {
        when(cityRepository.findByName("Atlantis")).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            cityService.getCityByName("Atlantis");
        });

        assertThat(ex.getMessage()).contains("City not found with name: Atlantis");
    }

    @Test
    void createCity_ShouldSaveAndReturnCity() {
        CityDTO dto = CityDTO.builder()
                .name("Berlin")
                .country("Germany")
                .addressIds(List.of(10, 20))
                .build();

        Address a1 = new Address();
        a1.setId(10);
        Address a2 = new Address();
        a2.setId(20);
        Country country = new Country();
        City city = new City();

        when(addressRepository.findById(10)).thenReturn(Optional.of(a1));
        when(addressRepository.findById(20)).thenReturn(Optional.of(a2));
        when(countryRepository.findByName("Germany")).thenReturn(Optional.of(country));
        when(cityMapper.toEntity(dto, country, Set.of(a1, a2))).thenReturn(city);
        when(cityRepository.save(city)).thenReturn(city);

        City result = cityService.createCity(dto);

        assertSame(city, result);
        verify(addressRepository).findById(10);
        verify(addressRepository).findById(20);
        verify(countryRepository).findByName("Germany");
        verify(cityMapper).toEntity(dto, country, Set.of(a1, a2));
        verify(cityRepository).save(city);
    }

    @Test
    void createCity_WhenAddressNotFound_ShouldThrow() {
        CityDTO dto = CityDTO.builder()
                .name("Berlin")
                .country("Germany")
                .addressIds(List.of(10))
                .build();

        when(addressRepository.findById(10)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cityService.createCity(dto));
    }

    @Test
    void createCity_WhenCountryNotFound_ShouldThrow() {
        CityDTO dto = CityDTO.builder()
                .name("Berlin")
                .country("Neverland")
                .addressIds(Collections.emptyList())
                .build();

        when(countryRepository.findByName("Neverland")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cityService.createCity(dto));
    }

    @Test
    void updateCity_WhenFound_ShouldUpdateAndReturnCity() {
        CityDTO dto = CityDTO.builder()
                .name("UpdatedCity")
                .country("France")
                .build();

        City existingCity = new City();
        Country country = new Country();
        City savedCity = new City();

        when(cityRepository.findById(1)).thenReturn(Optional.of(existingCity));
        when(countryRepository.findByName("France")).thenReturn(Optional.of(country));
        when(cityRepository.save(existingCity)).thenReturn(savedCity);

        City result = cityService.updateCity(1, dto);

        assertSame(savedCity, result);
        assertEquals("UpdatedCity", existingCity.getName());
        assertSame(country, existingCity.getCountry());

        verify(cityRepository).findById(1);
        verify(countryRepository).findByName("France");
        verify(cityRepository).save(existingCity);
    }

    @Test
    void updateCity_WhenNotFound_ShouldThrow() {
        CityDTO dto = new CityDTO();
        when(cityRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cityService.updateCity(1, dto));
    }

    @Test
    void updateCity_WhenCountryNotFound_ShouldThrow() {
        CityDTO dto = CityDTO.builder().country("Mars").build();
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
