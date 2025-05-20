package com.movieshop.server.service;

import com.movieshop.server.domain.Address;
import com.movieshop.server.domain.Store;
import com.movieshop.server.domain.User;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.UserMapper;
import com.movieshop.server.model.UserRequestDTO;
import com.movieshop.server.model.UserResponseDTO;
import com.movieshop.server.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
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
    public UserResponseDTO updateUser(Integer id, UserRequestDTO userRequestDTO) {
        User existentUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        existentUser.setEmail(userRequestDTO.getEmail());
        existentUser.setRole(userRequestDTO.getRole());
        existentUser.setFirstName(userRequestDTO.getFirstName());
        existentUser.setLastName(userRequestDTO.getLastName());
        existentUser.setPicture(userRequestDTO.getPicture());
        existentUser.setAccountNonExpired(userRequestDTO.isAccountNonExpired());
        existentUser.setAccountNonLocked(userRequestDTO.isAccountNonLocked());
        existentUser.setCredentialsNonExpired(userRequestDTO.isCredentialsNonExpired());
        existentUser.setEnabled(userRequestDTO.isEnabled());

        Address address = userRequestDTO.getAddress();
        if (address != null) {
            existentUser.setAddress(address);
        }
        Store store = userRequestDTO.getStore();
        if (store != null) {
            existentUser.setStore(store);
        }

        existentUser = userRepository.save(existentUser);

        return userMapper.toResponseDto(existentUser);
    }

    @Override
    public void deleteUser(Integer id) {
        User existentUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(existentUser);
    }
}
