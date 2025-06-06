package com.movieshop.server.service;

import com.movieshop.server.domain.Address;
import com.movieshop.server.domain.City;
import com.movieshop.server.domain.Country;
import com.movieshop.server.domain.User;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.AddressMapper;
import com.movieshop.server.mapper.CityMapper;
import com.movieshop.server.mapper.CountryMapper;
import com.movieshop.server.model.AddressRequestDTO;
import com.movieshop.server.model.AddressResponseDTO;
import com.movieshop.server.model.CityResponseDTO;
import com.movieshop.server.model.CountryResponseDTO;
import com.movieshop.server.repository.AddressRepository;
import com.movieshop.server.repository.CityRepository;
import com.movieshop.server.repository.CountryRepository;
import com.movieshop.server.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements IAddressService{

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final CityRepository cityRepository;
    private final UserRepository userRepository;
    private final CountryRepository countryRepository;
    private final CityMapper cityMapper;
    private final CountryMapper countryMapper;

    public AddressServiceImpl(
            AddressRepository addressRepository,
            AddressMapper addressMapper,
            CityRepository cityRepository, UserRepository userRepository, CountryRepository countryRepository, CityMapper cityMapper, CountryMapper countryMapper
    ) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
        this.cityRepository = cityRepository;
        this.userRepository = userRepository;
        this.countryRepository = countryRepository;
        this.cityMapper = cityMapper;
        this.countryMapper = countryMapper;
    }


    @Override
    public List<AddressResponseDTO> getAllAddresses() {
        List<Address> addresses = addressRepository.findAll();
        if (addresses.isEmpty()) {
            return List.of();
        }
        return addresses.stream()
                .map(addressMapper::toResponseDto)
                .toList();
    }

    @Override
    public AddressResponseDTO getAddressById(Integer id) {
        Address address = getAddressByIdElseThrow(id);
        return addressMapper.toResponseDto(address);
    }

    @Transactional
    @Override
    public AddressResponseDTO createAddress(AddressRequestDTO addressRequestDTO) {
        City city = fetchOrCreateCity(addressRequestDTO);
        Country country = countryRepository.findByName(addressRequestDTO.getCountry())
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with name: " + addressRequestDTO.getCountry()));
        User user = userRepository.findById(addressRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + addressRequestDTO.getUserId()));

        Address address = addressMapper.toEntity(addressRequestDTO);
        address.setCity(city);

        Address savedAddress = addressRepository.save(address);

        user.setAddress(savedAddress);
        userRepository.save(user);

        AddressResponseDTO responseDto = addressMapper.toResponseDto(savedAddress);

        CountryResponseDTO countryResponseDto = countryMapper.toResponseDto(country);
        CityResponseDTO cityResponseDto = cityMapper.toResponseDto(city);

        cityResponseDto.setCountry(countryResponseDto);
        responseDto.setCity(cityResponseDto);

        return responseDto;
    }

    @Transactional
    @Override
    public AddressResponseDTO updateAddress(Integer id, AddressRequestDTO addressRequestDTO) {
        Address existingAddress = getAddressByIdElseThrow(id);
        City city = fetchOrCreateCity(addressRequestDTO);
        existingAddress.setCity(city);

        existingAddress.setAddress(addressRequestDTO.getAddress());
        existingAddress.setAddress2(addressRequestDTO.getAddress2());
        existingAddress.setDistrict(addressRequestDTO.getDistrict());
        existingAddress.setPostalCode(addressRequestDTO.getPostalCode());
        existingAddress.setPhone(addressRequestDTO.getPhone());

        existingAddress = addressRepository.save(existingAddress);

        return addressMapper.toResponseDto(existingAddress);
    }

    @Override
    public void deleteAddress(Integer id) {
        Address existingAddress = getAddressByIdElseThrow(id);
        addressRepository.deleteById(id);
    }

    private Address getAddressByIdElseThrow(Integer id) {
        return addressRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Address not found with id: " + id));
    }

    private City fetchOrCreateCity(AddressRequestDTO dto) {
        Country country = countryRepository.findByName(dto.getCountry()).orElseThrow(() ->
                new ResourceNotFoundException("Country not found with name: " + dto.getCountry()));

        return cityRepository.findByNameAndCountry(dto.getCity(), country)
                .orElseGet(() -> cityRepository.save(new City(dto.getCity(), country)));
    }
}
