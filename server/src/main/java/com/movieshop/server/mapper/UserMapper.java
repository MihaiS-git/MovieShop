package com.movieshop.server.mapper;

import com.movieshop.server.domain.Rental;
import com.movieshop.server.domain.User;
import com.movieshop.server.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@RequiredArgsConstructor
@Component
public class UserMapper {

    private final AddressMapper addressMapper;

    public UserResponseDTO toResponseDto(User user) {
        if (user == null) {
            return null;
        }
        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role((user.getRole().toString()))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .addressId(user.getAddress() != null ? user.getAddress().getId() : null)
                .storeId(user.getStore() != null ? user.getStore().getId() : null)
                .picture(user.getPicture())
                .accountNonExpired(user.isAccountNonExpired())
                .accountNonLocked(user.isAccountNonLocked())
                .credentialsNonExpired(user.isCredentialsNonExpired())
                .enabled(user.isEnabled())
                .createAt(user.getCreateAt())
                .lastUpdate(user.getLastUpdate())
                .build();
    }

    public UserResponseWithAddressDTO toResponseWithAddressDto(User user) {
        if (user == null) {
            return null;
        }
        return UserResponseWithAddressDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role((user.getRole().toString()))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .address(user.getAddress() != null ? addressMapper.toResponseDto(user.getAddress()) : null)
                .storeId(user.getStore().getId())
                .picture(user.getPicture())
                .rentalIds(
                        user.getRentals() != null
                        ? user.getRentals().stream().map(Rental::getId).toList()
                        : new ArrayList<>()
                )
                .accountNonExpired(user.isAccountNonExpired())
                .accountNonLocked(user.isAccountNonLocked())
                .credentialsNonExpired(user.isCredentialsNonExpired())
                .enabled(user.isEnabled())
                .createAt(user.getCreateAt())
                .lastUpdate(user.getLastUpdate())
                .customerPaymentIds(user.getCustomerPayments() != null
                        ? user.getCustomerPayments().stream().map((p) -> p.getPaymentId()).toList()
                        : new ArrayList<>()
                )
                .staffPaymentIds(user.getStaffPayments() != null
                        ? user.getStaffPayments().stream().map((p) -> p.getPaymentId()).toList()
                        : new ArrayList<>()
                )
                .build();
    }

    private UserResponseWithDetailsDTO mapToUserDto(UserWithAddressProjection projection) {
        var address = projection.getAddress();
        AddressResponseDTO addressDto = null;

        if (address != null) {
            var city = address.getCity();
            CityResponseDTO cityDto = null;
            if (city != null) {
                var country = city.getCountry();
                CountryResponseDTO countryDto = (country != null)
                        ? new CountryResponseDTO(country.getId(), country.getName(), country.getLastUpdate())
                        : null;

                cityDto = new CityResponseDTO(city.getId(), city.getName(), countryDto, city.getLastUpdate());
            }

            addressDto = AddressResponseDTO.builder()
                    .id(address.getId())
                    .address(address.getAddress())
                    .address2(address.getAddress2())
                    .district(address.getDistrict())
                    .phone(address.getPhone())
                    .postalCode(address.getPostalCode())
                    .city(cityDto)
                    .build();
        }

        return new UserResponseWithDetailsDTO(
                projection.getId(),
                projection.getEmail(),
                projection.getRole(),
                projection.getFirstName(),
                projection.getLastName(),
                addressDto,
                projection.getStore() != null ? projection.getStore().getId() : null,
                projection.getPicture(),
                null,
                projection.isAccountNonExpired(),
                projection.isAccountNonLocked(),
                projection.isCredentialsNonExpired(),
                projection.isEnabled(),
                projection.getCreateAt(),
                projection.getLastUpdate(),
                null,
                null
        );
    }

    public UserResponseItemDTO toResponseItemDTO(User user) {
        if (user == null) {
            return null;
        }
        return UserResponseItemDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole().toString())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .accountNonExpired(user.isAccountNonExpired())
                .accountNonLocked(user.isAccountNonLocked())
                .credentialsNonExpired(user.isCredentialsNonExpired())
                .enabled(user.isEnabled())
                .createAt(user.getCreateAt())
                .lastUpdate(user.getLastUpdate())
                .build();
    }

    public User toEntity(UserRequestDTO userRequestDTO) {
        if (userRequestDTO == null) {
            return null;
        }
        User user = new User();
        user.setEmail(userRequestDTO.getEmail());
        user.setRole(userRequestDTO.getRole());
        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
        user.setPicture(userRequestDTO.getPicture());
        user.setAccountNonExpired(userRequestDTO.isAccountNonExpired());
        user.setAccountNonLocked(userRequestDTO.isAccountNonLocked());
        user.setCredentialsNonExpired(userRequestDTO.isCredentialsNonExpired());
        user.setEnabled(userRequestDTO.isEnabled());
        return user;
    }
}
