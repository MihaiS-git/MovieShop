package com.movieshop.server.mapper;

import com.movieshop.server.domain.Address;
import com.movieshop.server.domain.City;
import com.movieshop.server.model.AddressDTO;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressDTO toDto(Address address) {
        if (address == null) {
            return null;
        }
        return AddressDTO.builder()
                .id(address.getId())
                .address(address.getAddress())
                .address2(address.getAddress2())
                .district(address.getDistrict())
                .city(address.getCity().getName())
                .postalCode(address.getPostalCode())
                .phone(address.getPhone())
                .build();
    }

    public Address toEntity(AddressDTO addressDTO, City city) {
        if (addressDTO == null) {
            return null;
        }
        Address address = new Address();
        address.setAddress(addressDTO.getAddress());
        address.setAddress2(addressDTO.getAddress2());
        address.setDistrict(addressDTO.getDistrict());
        address.setCity(city);
        address.setPostalCode(addressDTO.getPostalCode());
        address.setPhone(addressDTO.getPhone());
        return address;
    }
}
