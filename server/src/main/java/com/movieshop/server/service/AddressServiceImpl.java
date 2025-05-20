package com.movieshop.server.service;

import com.movieshop.server.domain.Address;
import com.movieshop.server.domain.City;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.AddressMapper;
import com.movieshop.server.model.AddressRequestDTO;
import com.movieshop.server.model.AddressResponseDTO;
import com.movieshop.server.repository.AddressRepository;
import com.movieshop.server.repository.CityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements IAddressService{

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final CityRepository cityRepository;

    public AddressServiceImpl(
            AddressRepository addressRepository,
            AddressMapper addressMapper,
            CityRepository cityRepository
    ) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
        this.cityRepository = cityRepository;
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

    @Override
    public AddressResponseDTO createAddress(AddressRequestDTO addressRequestDTO) {
        City city = getCityByNameElseThrow(addressRequestDTO);

        Address address = addressMapper.toEntity(addressRequestDTO);
        city.addAddress(address);

        address = addressRepository.save(address);

        return addressMapper.toResponseDto(address);
    }


    @Override
    public AddressResponseDTO updateAddress(Integer id, AddressRequestDTO addressRequestDTO) {
        Address existingAddress = getAddressByIdElseThrow(id);
        City city = getCityByNameElseThrow(addressRequestDTO);
        city.addAddress(existingAddress);

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

    private City getCityByNameElseThrow(AddressRequestDTO addressRequestDTO) {
        return cityRepository.findByName(addressRequestDTO.getCity()).orElseThrow(() ->
                new ResourceNotFoundException("City not found with name: " + addressRequestDTO.getCity()));
    }
}
