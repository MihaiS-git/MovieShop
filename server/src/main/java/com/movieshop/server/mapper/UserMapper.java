package com.movieshop.server.mapper;

import com.movieshop.server.domain.Payment;
import com.movieshop.server.domain.Rental;
import com.movieshop.server.domain.User;
import com.movieshop.server.model.UserRequestDTO;
import com.movieshop.server.model.UserResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
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
                .rentalIds(user.getRentals() != null
                ? user.getRentals().stream()
                        .map(Rental::getId)
                        .toList()
                        : null)
                .accountNonExpired(user.isAccountNonExpired())
                .accountNonLocked(user.isAccountNonLocked())
                .credentialsNonExpired(user.isCredentialsNonExpired())
                .enabled(user.isEnabled())
                .createAt(user.getCreateAt())
                .lastUpdate(user.getLastUpdate())
                .customerPaymentIds(user.getCustomerPayments() != null
                        ? user.getCustomerPayments().stream()
                                .map(Payment::getPaymentId)
                                .toList()
                        : null)
                .staffPaymentIds(user.getStaffPayments() != null
                        ? user.getStaffPayments().stream()
                                .map(Payment::getPaymentId)
                                .toList()
                        : null)
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
