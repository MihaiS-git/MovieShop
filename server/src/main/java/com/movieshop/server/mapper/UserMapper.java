package com.movieshop.server.mapper;

import com.movieshop.server.domain.User;
import com.movieshop.server.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
                .picture(user.getPicture())
                .accountNonExpired(user.isAccountNonExpired())
                .accountNonLocked(user.isAccountNonLocked())
                .credentialsNonExpired(user.isCredentialsNonExpired())
                .enabled(user.isEnabled())
                .createAt(user.getCreateAt())
                .lastUpdate(user.getLastUpdate())
                .address(user.getAddress() != null ? addressMapper.toResponseDto(user.getAddress()) : null)
                .build();
    }

    public UserResponseItemDTO toResponseItemDTO(User user) {
        if( user == null) {
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
