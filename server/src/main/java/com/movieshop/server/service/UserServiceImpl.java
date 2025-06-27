package com.movieshop.server.service;

import com.movieshop.server.UserSpecifications;
import com.movieshop.server.domain.User;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.AddressMapper;
import com.movieshop.server.mapper.UserMapper;
import com.movieshop.server.model.*;
import com.movieshop.server.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;

    public UserServiceImpl(
            UserRepository userRepository,
            UserMapper userMapper, AddressMapper addressMapper
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.addressMapper = addressMapper;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::toResponseDto).toList();
    }


    @Transactional
    @Override
    public UserResponseWithAddressDTO getUserById(Integer userId) {
        UserWithAddressProjection user = userRepository.findUserWithAddressById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        List<Integer> rentalIds = userRepository.findRentalIdsByUserId(userId);
        List<Integer> customerPaymentIds = userRepository.findCustomerPaymentIdsByUserId(userId);
        List<Integer> staffPaymentIds = userRepository.findStaffPaymentIdsByUserId(userId);

        return UserResponseWithAddressDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .address(addressMapper.projectionToResponseDto(user.getAddress()))
                .storeId(user.getStore().getId())
                .picture(user.getPicture())
                .accountNonExpired(user.isAccountNonExpired())
                .accountNonLocked(user.isAccountNonLocked())
                .credentialsNonExpired(user.isCredentialsNonExpired())
                .enabled(user.isEnabled())
                .createAt(user.getCreateAt())
                .lastUpdate(user.getLastUpdate())
                .rentalIds(rentalIds)
                .customerPaymentIds(customerPaymentIds)
                .staffPaymentIds(staffPaymentIds)
                .build();
    }

    @Override
    public UserResponseDTO updateUser(Integer id, UserUpdateRequestDTO userRequestDTO) {
        User existentUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        existentUser.setFirstName(userRequestDTO.getFirstName());
        existentUser.setLastName(userRequestDTO.getLastName());
        existentUser.setPicture(userRequestDTO.getPicture());

        existentUser = userRepository.save(existentUser);

        return userMapper.toResponseDto(existentUser);
    }

    @Override
    public void deleteUser(Integer id) {
        User existentUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(existentUser);
    }

    @Override
    public UserResponseWithAddressDTO getUserDetailsByEmail(String email) {
        User user = userRepository.findByEmailWithAddress(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        return userMapper.toResponseWithAddressDto(user);
    }

    @Override
    public UserPageResponse getAllUsersPaginated(Integer page, Integer limit, String orderBy,
                                                 String roleFilter, String searchFilter,
                                                 Boolean enabledFilter, Boolean accountNonExpiredFilter,
                                                 Boolean accountNonLockedFilter, Boolean credentialsNonExpiredFilter) {
        Sort sort = parseOrderBy(orderBy);

        Pageable pageable = PageRequest.of(page, limit, sort);

        Specification<User> spec = Specification
                .where(UserSpecifications.hasRole(roleFilter))
                .and(UserSpecifications.generalSearchContains(searchFilter))
                .and(UserSpecifications.isEnabled(enabledFilter))
                .and(UserSpecifications.isAccountNonExpired(accountNonExpiredFilter))
                .and(UserSpecifications.isAccountNonLocked(accountNonLockedFilter))
                .and(UserSpecifications.isCredentialsNonExpired(credentialsNonExpiredFilter));

        Page<User> userPage = userRepository.findAll(spec, pageable);

        List<UserResponseItemDTO> userDtos = userPage.getContent().stream()
                .map(userMapper::toResponseItemDTO)
                .toList();
        return new UserPageResponse(userDtos, userPage.getTotalElements());
    }

    @Override
    public UserResponseWithAddressDTO updateUserAccount(Integer id, UserAccountUpdateRequestDTO userRequestDTO) {
        User existentUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        existentUser.setAccountNonExpired(userRequestDTO.isAccountNonExpired());
        existentUser.setAccountNonLocked(userRequestDTO.isAccountNonLocked());
        existentUser.setCredentialsNonExpired(userRequestDTO.isCredentialsNonExpired());
        existentUser.setEnabled(userRequestDTO.isEnabled());

        userRepository.save(existentUser);

        UserWithAddressProjection user = userRepository.findUserWithAddressById(existentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + existentUser.getId()));

        List<Integer> rentalIds = userRepository.findRentalIdsByUserId(user.getId());
        List<Integer> customerPaymentIds = userRepository.findCustomerPaymentIdsByUserId(user.getId());
        List<Integer> staffPaymentIds = userRepository.findStaffPaymentIdsByUserId(user.getId());

        return UserResponseWithAddressDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .address(addressMapper.projectionToResponseDto(user.getAddress()))
                .storeId(user.getStore().getId())
                .picture(user.getPicture())
                .accountNonExpired(user.isAccountNonExpired())
                .accountNonLocked(user.isAccountNonLocked())
                .credentialsNonExpired(user.isCredentialsNonExpired())
                .enabled(user.isEnabled())
                .createAt(user.getCreateAt())
                .lastUpdate(user.getLastUpdate())
                .rentalIds(rentalIds)
                .customerPaymentIds(customerPaymentIds)
                .staffPaymentIds(staffPaymentIds)
                .build();
    }

    private Sort parseOrderBy(String orderBy) {
        if (orderBy == null || orderBy.isEmpty()) {
            return Sort.by(Sort.Direction.ASC, "id");
        }

        String[] parts = orderBy.split("_", 2);
        if (parts.length != 2) {
            log.warn("Invalid orderBy format '{}', falling back to id_asc", orderBy);
            return Sort.by(Sort.Direction.ASC, "id");
        }
        String field = parts[0];
        Sort.Direction direction;
        try {
            direction = Sort.Direction.fromString(parts[1]);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid sort direction '{}', falling back to id_asc", parts[1]);
            direction = Sort.Direction.ASC;
        }
        return Sort.by(direction, field);
    }
}
