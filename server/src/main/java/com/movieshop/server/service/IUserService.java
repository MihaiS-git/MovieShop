package com.movieshop.server.service;

import com.movieshop.server.domain.User;
import com.movieshop.server.model.UserPageResponse;
import com.movieshop.server.model.UserResponseDTO;
import com.movieshop.server.model.UserResponseWithAddressAndStoreDTO;
import com.movieshop.server.model.UserUpdateRequestDTO;

import java.util.List;

public interface IUserService {
    User getUserByEmail(String email);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(Integer id);

    UserResponseDTO updateUser(Integer id, UserUpdateRequestDTO userUpdateRequestDTO);

    void deleteUser(Integer id);

    UserResponseWithAddressAndStoreDTO getUserDetailsByEmail(String email);

    UserPageResponse getAllUsersPaginated(Integer page, Integer limit, String orderBy,
                                          String roleFilter, String searchFilter,
                                          Boolean enabledFilter, Boolean accountNonExpiredFilter,
                                          Boolean accountNonLockedFilter, Boolean credentialsNonExpiredFilter);
}