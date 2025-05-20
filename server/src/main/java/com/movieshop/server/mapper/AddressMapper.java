package com.movieshop.server.mapper;

import com.movieshop.server.domain.Address;
import com.movieshop.server.model.AddressRequestDTO;
import com.movieshop.server.model.AddressResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressResponseDTO toResponseDto(Address address) {
        if (address == null) {
            return null;
        }
        return AddressResponseDTO.builder()
                .id(address.getId())
                .address(address.getAddress())
                .address2(address.getAddress2())
                .district(address.getDistrict())
                .city(address.getCity().getName())
                .postalCode(address.getPostalCode())
                .phone(address.getPhone())
                .build();
    }

    public Address toEntity(AddressRequestDTO addressRequestDTO) {
        if (addressRequestDTO == null) {
            return null;
        }
        Address address = new Address();
        address.setAddress(addressRequestDTO.getAddress());
        address.setAddress2(addressRequestDTO.getAddress2());
        address.setDistrict(addressRequestDTO.getDistrict());
        address.setPostalCode(addressRequestDTO.getPostalCode());
        address.setPhone(addressRequestDTO.getPhone());
        return address;
    }
}
