package com.movieshop.server.service;

import com.movieshop.server.UserSpecifications;
import com.movieshop.server.domain.User;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.UserMapper;
import com.movieshop.server.model.*;
import com.movieshop.server.repository.UserRepository;
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

    public UserServiceImpl(
            UserRepository userRepository,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
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

    @Override
    public UserResponseDTO getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toResponseDto(user);
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
