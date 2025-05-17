package com.movieshop.server.service;

import com.movieshop.server.domain.Address;
import com.movieshop.server.domain.City;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.AddressMapper;
import com.movieshop.server.model.AddressDTO;
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
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    @Override
    public Address getAddressById(Integer id) {
        return getAddressByIdElseThrow(id);
    }

    @Override
    public Address createAddress(AddressDTO addressDTO) {
        City city = getCityByNameElseThrow(addressDTO);
        Address address = addressMapper.toEntity(addressDTO, city);

        return addressRepository.save(address);
    }


    @Override
    public Address updateAddress(Integer id, AddressDTO addressDTO) {
        Address existingAddress = getAddressByIdElseThrow(id);
        City city = getCityByNameElseThrow(addressDTO);

        existingAddress.setAddress(addressDTO.getAddress());
        existingAddress.setAddress2(addressDTO.getAddress2());
        existingAddress.setDistrict(addressDTO.getDistrict());
        existingAddress.setCity(city);
        existingAddress.setPostalCode(addressDTO.getPostalCode());
        existingAddress.setPhone(addressDTO.getPhone());

        return addressRepository.save(existingAddress);
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

    private City getCityByNameElseThrow(AddressDTO addressDTO) {
        return cityRepository.findByName(addressDTO.getCity()).orElseThrow(() ->
                new ResourceNotFoundException("City not found with name: " + addressDTO.getCity()));
    }
}
