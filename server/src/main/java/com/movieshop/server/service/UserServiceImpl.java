package com.movieshop.server.service;

import com.movieshop.server.domain.User;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.UserMapper;
import com.movieshop.server.model.UserResponseDTO;
import com.movieshop.server.model.UserResponseWithAddressAndStoreDTO;
import com.movieshop.server.model.UserUpdateRequestDTO;
import com.movieshop.server.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
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
    public UserResponseWithAddressAndStoreDTO getUserDetailsByEmail(String email) {
        User user = userRepository.findByEmailWithAddressAndStore(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        return userMapper.toResponseWithAddressAndStoreDto(user);
    }
}
