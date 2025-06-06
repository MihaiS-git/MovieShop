package com.movieshop.server.mapper;

import com.movieshop.server.domain.Address;
import com.movieshop.server.model.AddressRequestDTO;
import com.movieshop.server.model.AddressResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AddressMapper {

    public final CityMapper cityMapper;

    public AddressResponseDTO toResponseDto(Address address) {
        if (address == null) {
            return null;
        }
        return AddressResponseDTO.builder()
                .id(address.getId())
                .address(address.getAddress())
                .address2(address.getAddress2())
                .district(address.getDistrict())
                .city(cityMapper.toResponseDto(address.getCity()))
                .postalCode(address.getPostalCode())
                .phone(address.getPhone())
                .lastUpdate(address.getLastUpdate())
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
