package com.movieshop.server.mapper;

import com.movieshop.server.domain.User;
import com.movieshop.server.model.UserRequestDTO;
import com.movieshop.server.model.UserResponseDTO;
import com.movieshop.server.model.UserResponseWithAddressAndStoreDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserMapper {

    private final AddressMapper addressMapper;
    private final StoreMapper storeMapper;

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

    public UserResponseWithAddressAndStoreDTO toResponseWithAddressAndStoreDto(User user) {
        if (user == null) {
            return null;
        }
        return UserResponseWithAddressAndStoreDTO.builder()
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
                .store(user.getStore() != null ? storeMapper.toCustomerResponseDto(user.getStore()) : null)
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
