package com.movieshop.server.service;

import com.movieshop.server.domain.Address;
import com.movieshop.server.model.AddressDTO;

import java.util.List;

public interface IAddressService {
    List<Address> getAllAddresses();
    Address getAddressById(Integer id);
    Address createAddress(AddressDTO addressDTO);
    Address updateAddress(Integer id, AddressDTO addressDTO);
    void deleteAddress(Integer id);
}
