package com.movieshop.server.service;

import com.movieshop.server.model.AddressRequestDTO;
import com.movieshop.server.model.AddressResponseDTO;

import java.util.List;

public interface IAddressService {
    List<AddressResponseDTO> getAllAddresses();
    AddressResponseDTO getAddressById(Integer id);
    AddressResponseDTO createAddress(AddressRequestDTO addressRequestDTO);
    AddressResponseDTO updateAddress(Integer id, AddressRequestDTO addressRequestDTO);
    void deleteAddress(Integer id);
}
