package com.movieshop.server.service;

import com.movieshop.server.domain.User;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserRepository userRepository;
    private IUserService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void getUserByEmail_existingEmail_returnsUser() {
        // Arrange
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUserByEmail(email);

        // Assert
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void getUserByEmail_nonExistingEmail_throwsException() {
        // Arrange
        String email = "missing@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserByEmail(email));
        assertTrue(thrown.getMessage().contains(email));
        verify(userRepository, times(1)).findByEmail(email);
    }
}
