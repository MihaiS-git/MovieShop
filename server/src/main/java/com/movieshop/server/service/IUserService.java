package com.movieshop.server.service;

import com.movieshop.server.domain.User;
import com.movieshop.server.model.UserRequestDTO;
import com.movieshop.server.model.UserResponseDTO;

import java.util.List;

public interface IUserService {
    User getUserByEmail(String email);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(Integer id);

    UserResponseDTO updateUser(Integer id, UserRequestDTO userRequestDTO);

    void deleteUser(Integer id);
}
