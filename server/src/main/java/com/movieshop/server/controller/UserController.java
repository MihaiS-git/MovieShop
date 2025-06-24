package com.movieshop.server.controller;

import com.movieshop.server.model.*;
import com.movieshop.server.service.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v0/users")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping
    public ResponseEntity<UserPageResponse> getAllUsersPaginated(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer limit,
            @RequestParam(defaultValue = "id_asc") String orderBy,
            @RequestParam(required = false) String roleFilter,
            @RequestParam(required = false) String searchFilter,
            @RequestParam(required = false) Boolean enabledFilter,
            @RequestParam(required = false) Boolean accountNonExpiredFilter,
            @RequestParam(required = false) Boolean accountNonLockedFilter,
            @RequestParam(required = false) Boolean credentialsNonExpiredFilter
            ) {

        return ResponseEntity.ok(userService.getAllUsersPaginated(page, limit, orderBy, roleFilter, searchFilter, enabledFilter, accountNonExpiredFilter, accountNonLockedFilter, credentialsNonExpiredFilter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Integer id) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseWithAddressDTO> getUserDetailsByEmail(@PathVariable String email) {
        UserResponseWithAddressDTO user = userService.getUserDetailsByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Integer id, @RequestBody UserUpdateRequestDTO userRequestDTO) {
        UserResponseDTO updatedUser = userService.updateUser(id, userRequestDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
