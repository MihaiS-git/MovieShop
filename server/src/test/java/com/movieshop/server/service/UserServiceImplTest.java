package com.movieshop.server.service;

import com.movieshop.server.domain.User;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.UserMapper;
import com.movieshop.server.model.UserResponseDTO;
import com.movieshop.server.model.UserUpdateRequestDTO;
import com.movieshop.server.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testGetUserByEmail_Success() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        User result = userService.getUserByEmail("test@example.com");

        assertEquals("test@example.com", result.getEmail());
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void testGetUserByEmail_NotFound() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserByEmail("missing@example.com"));
    }

    @Test
    void testGetAllUsers_ReturnsMappedList() {
        User user1 = new User();
        User user2 = new User();
        List<User> users = List.of(user1, user2);

        UserResponseDTO dto1 = new UserResponseDTO();
        UserResponseDTO dto2 = new UserResponseDTO();

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toResponseDto(user1)).thenReturn(dto1);
        when(userMapper.toResponseDto(user2)).thenReturn(dto2);

        List<UserResponseDTO> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));
    }

    @Test
    void testGetUserById_Success() {
        User user = new User();
        user.setId(1);
        UserResponseDTO dto = new UserResponseDTO();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userMapper.toResponseDto(user)).thenReturn(dto);

        UserResponseDTO result = userService.getUserById(1);

        assertEquals(dto, result);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserById(99));
    }

//    @Test
//    void testUpdateUser_Success_WithAddressAndStore() {
//        User existingUser = new User();
//        existingUser.setId(1);
//
//        UserUpdateRequestDTO dto = UserUpdateRequestDTO.builder()
//                .firstName("Updated")
//                .lastName("User")
//                .picture("pic.png")
//                .build();
//
//        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
//        when(userRepository.save(any())).thenReturn(existingUser);
//        when(userMapper.toResponseDto(existingUser)).thenReturn(new UserResponseDTO());
//
//        UserResponseDTO result = userService.updateUser(1, dto);
//
//        assertNotNull(result);
//        verify(userRepository).save(existingUser);
//        assertEquals("updated@example.com", existingUser.getEmail());
//        assertEquals(1, existingUser.getAddress().getId());
//        assertEquals(1, existingUser.getStore().getId());
//    }

    @Test
    void testUpdateUser_WithNullAddressAndStore() {
        User existingUser = new User();
        existingUser.setId(2);

        UserUpdateRequestDTO dto = UserUpdateRequestDTO.builder()
                .firstName("Null")
                .lastName("Check")
                .picture(null)
                .build();

        when(userRepository.findById(2)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any())).thenReturn(existingUser);
        when(userMapper.toResponseDto(existingUser)).thenReturn(new UserResponseDTO());

        UserResponseDTO result = userService.updateUser(2, dto);

        assertNotNull(result);
        assertNull(existingUser.getAddress());
        assertNull(existingUser.getStore());
    }

    @Test
    void testUpdateUser_UserNotFound() {
        when(userRepository.findById(123)).thenReturn(Optional.empty());

        UserUpdateRequestDTO dto = UserUpdateRequestDTO.builder().build();

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(123, dto));
    }

    @Test
    void testDeleteUser_Success() {
        User user = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        userService.deleteUser(1);

        verify(userRepository).delete(user);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.findById(404)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(404));
    }
}
