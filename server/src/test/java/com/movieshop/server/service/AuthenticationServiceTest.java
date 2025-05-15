package com.movieshop.server.service;

import com.movieshop.server.domain.Role;
import com.movieshop.server.domain.User;
import com.movieshop.server.exception.InvalidAuthException;
import com.movieshop.server.model.AuthenticationRequest;
import com.movieshop.server.model.RegisterRequest;
import com.movieshop.server.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthenticationServiceTest {

    // Mock dependencies
    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        // Create a RegisterRequest object with test data
        RegisterRequest registerRequest = new RegisterRequest("testuser@example.com", "password123", "Test User", "http://example.com/picture.jpg");

        // Create mock User object
        User user = User.builder()
                .email(registerRequest.getEmail())
                .password("passwordEncoded")
                .role(Role.CUSTOMER)
                .name(registerRequest.getName())
                .picture(registerRequest.getPicture())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();

        // Mock the behaviour of the password encoder
        when(passwordEncoder.encode(anyString())).thenReturn("passwordEncoded");

        // Mock the behaviour of the user repository save method
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Mock the behaviour of the JWT service
        when(jwtService.generateToken(any(User.class))).thenReturn("mockedJwtToken");

        // Call the register method
        String token = authenticationService.register(registerRequest);

        // Verify the result
        assertNotNull(token);
        assertEquals("mockedJwtToken", token);

        // Verify that methods were called
        verify(userRepository).save(any(User.class));
        verify(jwtService).generateToken(any(User.class));
    }

    @Test
    void testRegisterThrowsException() {
        RegisterRequest request = new RegisterRequest("fail@example.com", "pass", "Fail User", null);

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Database is down"));

        InvalidAuthException exception = assertThrows(InvalidAuthException.class, () -> authenticationService.register(request));

        assertTrue(exception.getMessage().contains("An error occurred during registration"));
    }


    @Test
    void testAuthenticate() {
        AuthenticationRequest request = new AuthenticationRequest("testuser@example.com", "password123");

        User user = User.builder()
                .email(request.getEmail())
                .password("passwordEncoded")
                .role(Role.CUSTOMER)
                .name("Test User")
                .picture("http://example.com/picture.jpg")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();

        // Mock the behaviour of the authentication manager
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);

        // Mock the behaviour of the userRepository
        when(userRepository.findByEmail(request.getEmail())).thenReturn(java.util.Optional.of(user));

        // Mock the behaviour of the JWT service
        when(jwtService.generateToken(any(User.class))).thenReturn("mockedJwtToken");

        // Call the authenticate method
        String token = authenticationService.authenticate(request);

        // Verify the result
        assertNotNull(token);
        assertEquals("mockedJwtToken", token);

        // Verify that methods were called
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail(request.getEmail());
        verify(jwtService).generateToken(any(User.class));
    }

    @Test
    void testAuthenticateWithInvalidCredentials() {
        AuthenticationRequest request = new AuthenticationRequest("wronguser@example.com", "wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid email or password"));

        // Call the authenticate method and expect an exception
        assertThrows(InvalidAuthException.class, () -> authenticationService.authenticate(request));
    }

    @Test
    void testAuthenticateWithDisabledAccount() {
        AuthenticationRequest request = new AuthenticationRequest("wronguser@example.com", "wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new DisabledException("Account is disabled"));

        // Call the authenticate method and expect an exception
        assertThrows(InvalidAuthException.class, () -> authenticationService.authenticate(request));
    }

    @Test
    void testAuthenticateWithAccountLocked() {
        AuthenticationRequest request = new AuthenticationRequest("wronguser@example.com", "wrongpassword");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new LockedException("Account is locked"));

        // Call the authenticate method and expect an exception
        assertThrows(InvalidAuthException.class, () -> authenticationService.authenticate(request));
    }

    @Test
    void testAuthenticateUserNotFoundAfterAuth() {
        AuthenticationRequest request = new AuthenticationRequest("ghost@example.com", "pass");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.empty());

        InvalidAuthException exception = assertThrows(InvalidAuthException.class, () -> authenticationService.authenticate(request));
        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void testAuthenticateUserNotEnabled() {
        AuthenticationRequest request = new AuthenticationRequest("disabled@example.com", "pass");

        User disabledUser = User.builder()
                .email(request.getEmail())
                .enabled(false) // Important
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(disabledUser));

        InvalidAuthException exception = assertThrows(InvalidAuthException.class, () -> authenticationService.authenticate(request));
        assertEquals("Account is disabled", exception.getMessage());
    }

    @Test
    void testAuthenticateWithUnexpectedException() {
        AuthenticationRequest request = new AuthenticationRequest("user@example.com", "pass");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Something broke"));

        InvalidAuthException exception = assertThrows(InvalidAuthException.class, () -> authenticationService.authenticate(request));
        assertTrue(exception.getMessage().contains("Authentication failed"));
    }



}
