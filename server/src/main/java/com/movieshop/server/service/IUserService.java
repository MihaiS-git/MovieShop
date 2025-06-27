package com.movieshop.server.service;

import com.movieshop.server.domain.User;
import com.movieshop.server.model.*;

import java.util.List;

public interface IUserService {
    User getUserByEmail(String email);

    List<UserResponseDTO> getAllUsers();

    UserResponseWithAddressDTO getUserById(Integer id);

    UserResponseDTO updateUser(Integer id, UserUpdateRequestDTO userUpdateRequestDTO);

    void deleteUser(Integer id);

    UserResponseWithAddressDTO getUserDetailsByEmail(String email);

    UserPageResponse getAllUsersPaginated(Integer page, Integer limit, String orderBy,
                                          String roleFilter, String searchFilter,
                                          Boolean enabledFilter, Boolean accountNonExpiredFilter,
                                          Boolean accountNonLockedFilter, Boolean credentialsNonExpiredFilter);

    UserResponseWithAddressDTO updateUserAccount(Integer id, UserAccountUpdateRequestDTO userRequestDTO);
}