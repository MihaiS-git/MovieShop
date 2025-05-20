package com.movieshop.server.service;

import com.movieshop.server.domain.Address;
import com.movieshop.server.domain.City;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.AddressMapper;
import com.movieshop.server.model.AddressRequestDTO;
import com.movieshop.server.model.AddressResponseDTO;
import com.movieshop.server.repository.AddressRepository;
import com.movieshop.server.repository.CityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private AddressMapper addressMapper;

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

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
    void getAllAddresses_ShouldReturnAllAddresses() {
        List<Address> addresses = Arrays.asList(new Address(), new Address());
        when(addressRepository.findAll()).thenReturn(addresses);

        List<AddressResponseDTO> result = addressService.getAllAddresses();

        assertEquals(2, result.size());
        verify(addressRepository).findAll();
    }

    @Test
    void getAllAddresses_ShouldReturnEmptyList_WhenNoAddressesExist() {
        when(addressRepository.findAll()).thenReturn(Collections.emptyList());

        List<AddressResponseDTO> result = addressService.getAllAddresses();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(addressRepository).findAll();
    }


    @Test
    void getAddressById_WhenFound_ShouldReturnAddress() {
        Address address = new Address();
        when(addressRepository.findById(1)).thenReturn(Optional.of(address));

        AddressResponseDTO result = addressService.getAddressById(1);

        assertEquals(addressMapper.toResponseDto(address), result);
        verify(addressRepository).findById(1);
    }

    @Test
    void getAddressById_WhenNotFound_ShouldThrowException() {
        when(addressRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addressService.getAddressById(1));
    }

    @Test
    void createAddress_ShouldSaveAndReturnAddress() {
        // Arrange
        AddressRequestDTO dto = AddressRequestDTO.builder()
                .city("New York")
                .address("123 Test St")
                .build();

        City city = new City();
        city.setName("New York");

        Address addressEntity = new Address();
        addressEntity.setAddress("123 Test St");
        addressEntity.setCity(city);

        Address savedAddress = new Address();
        savedAddress.setId(1);
        savedAddress.setAddress("123 Test St");
        savedAddress.setCity(city);

        AddressResponseDTO responseDTO = AddressResponseDTO.builder()
                .id(1)
                .address("123 Test St")
                .city("New York")
                .build();

        when(cityRepository.findByName("New York")).thenReturn(Optional.of(city));
        when(addressMapper.toEntity(dto)).thenReturn(addressEntity);
        when(addressRepository.save(addressEntity)).thenReturn(savedAddress);
        when(addressMapper.toResponseDto(savedAddress)).thenReturn(responseDTO);

        // Act
        AddressResponseDTO result = addressService.createAddress(dto);

        // Assert
        assertEquals(responseDTO, result);
        verify(cityRepository).findByName("New York");
        verify(addressMapper).toEntity(dto);
        verify(addressRepository).save(addressEntity);
        verify(addressMapper).toResponseDto(savedAddress);
    }


    @Test
    void createAddress_WhenCityNotFound_ShouldThrowException() {
        AddressRequestDTO dto = AddressRequestDTO.builder()
                .city("New York")
                .build();

        when(cityRepository.findByName("Nonexistent")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addressService.createAddress(dto));
    }

    @Test
    void updateAddress_ShouldUpdateAndReturnAddress() {
        AddressRequestDTO dto = AddressRequestDTO.builder()
                .address("123 Main St")
                .address2("Suite 100")
                .district("Central")
                .city("New York")
                .postalCode("12345")
                .phone("555-1234")
                .build();

        Address existing = new Address();
        City city = new City();
        AddressResponseDTO responseDTO = AddressResponseDTO.builder()
                .address("123 Main St")
                .address2("Suite 100")
                .district("Central")
                .city("New York")
                .postalCode("12345")
                .phone("555-1234")
                .build();

        when(addressRepository.findById(1)).thenReturn(Optional.of(existing));
        when(cityRepository.findByName("New York")).thenReturn(Optional.of(city));
        when(addressRepository.save(existing)).thenReturn(existing);
        when(addressMapper.toResponseDto(existing)).thenReturn(responseDTO); // 👈 CRITICAL

        AddressResponseDTO result = addressService.updateAddress(1, dto);

        assertEquals("123 Main St", result.getAddress());
        assertEquals("Suite 100", result.getAddress2());
        assertEquals("Central", result.getDistrict());
        assertEquals("555-1234", result.getPhone());
        assertEquals("12345", result.getPostalCode());
        assertEquals("New York", result.getCity());
    }


    @Test
    void updateAddress_WhenNotFound_ShouldThrowException() {
        AddressRequestDTO dto = new AddressRequestDTO();
        when(addressRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addressService.updateAddress(1, dto));
    }

    @Test
    void updateAddress_WhenCityNotFound_ShouldThrowException() {
        AddressRequestDTO dto = AddressRequestDTO.builder()
                .city("Nowhere")
                .build();

        Address existing = new Address();
        when(addressRepository.findById(1)).thenReturn(Optional.of(existing));
        when(cityRepository.findByName("Nowhere")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addressService.updateAddress(1, dto));
    }

    @Test
    void deleteAddress_ShouldDeleteIfExists() {
        Address address = new Address();
        when(addressRepository.findById(1)).thenReturn(Optional.of(address));

        addressService.deleteAddress(1);

        verify(addressRepository).deleteById(1);
    }

    @Test
    void deleteAddress_WhenNotFound_ShouldThrowException() {
        when(addressRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addressService.deleteAddress(1));
    }
}
