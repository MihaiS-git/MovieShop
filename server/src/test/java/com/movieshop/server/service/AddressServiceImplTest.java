package com.movieshop.server.service;

import com.movieshop.server.domain.Address;
import com.movieshop.server.domain.City;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.AddressMapper;
import com.movieshop.server.model.AddressDTO;
import com.movieshop.server.repository.AddressRepository;
import com.movieshop.server.repository.CityRepository;
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

        List<Address> result = addressService.getAllAddresses();

        assertEquals(2, result.size());
        verify(addressRepository).findAll();
    }

    @Test
    void getAddressById_WhenFound_ShouldReturnAddress() {
        Address address = new Address();
        when(addressRepository.findById(1)).thenReturn(Optional.of(address));

        Address result = addressService.getAddressById(1);

        assertEquals(address, result);
        verify(addressRepository).findById(1);
    }

    @Test
    void getAddressById_WhenNotFound_ShouldThrowException() {
        when(addressRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addressService.getAddressById(1));
    }

    @Test
    void createAddress_ShouldSaveAndReturnAddress() {
        AddressDTO dto = AddressDTO.builder()
                .city("New York")
                .build();

        City city = new City();
        Address address = new Address();

        when(cityRepository.findByName("New York")).thenReturn(Optional.of(city));
        when(addressMapper.toEntity(dto, city)).thenReturn(address);
        when(addressRepository.save(address)).thenReturn(address);

        Address result = addressService.createAddress(dto);

        assertEquals(address, result);
        verify(cityRepository).findByName("New York");
        verify(addressRepository).save(address);
    }

    @Test
    void createAddress_WhenCityNotFound_ShouldThrowException() {
        AddressDTO dto = AddressDTO.builder()
                .city("New York")
                .build();

        when(cityRepository.findByName("Nonexistent")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addressService.createAddress(dto));
    }

    @Test
    void updateAddress_ShouldUpdateAndReturnAddress() {
        AddressDTO dto = AddressDTO.builder()
                .address("123 Main St")
                .address2("Suite 100")
                .district("Central")
                .city("New York")
                .postalCode("12345")
                .phone("555-1234")
                .build();

        Address existing = new Address();
        City city = new City();

        when(addressRepository.findById(1)).thenReturn(Optional.of(existing));
        when(cityRepository.findByName("New York")).thenReturn(Optional.of(city));
        when(addressRepository.save(existing)).thenReturn(existing);

        Address result = addressService.updateAddress(1, dto);

        assertEquals(existing, result);
        assertEquals("123 Main St", existing.getAddress());
        assertEquals("Suite 100", existing.getAddress2());
        assertEquals("Central", existing.getDistrict());
        assertEquals("555-1234", existing.getPhone());
        assertEquals("12345", existing.getPostalCode());
        assertEquals(city, existing.getCity());
    }

    @Test
    void updateAddress_WhenNotFound_ShouldThrowException() {
        AddressDTO dto = new AddressDTO();
        when(addressRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addressService.updateAddress(1, dto));
    }

    @Test
    void updateAddress_WhenCityNotFound_ShouldThrowException() {
        AddressDTO dto = AddressDTO.builder()
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
