package com.movieshop.server.service;

import com.movieshop.server.domain.Role;
import com.movieshop.server.domain.User;
import com.movieshop.server.exception.InvalidAuthException;
import com.movieshop.server.model.AuthenticationRequest;
import com.movieshop.server.model.RegisterRequest;
import com.movieshop.server.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            JwtService jwtService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager
    ) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public String register(RegisterRequest request) {
        try {
            User user = User.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.CUSTOMER)
                    .name(request.getName())
                    .picture(request.getPicture())
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .enabled(true)
                    .build();
            User savedUser = userRepository.save(user);

            return jwtService.generateToken(savedUser);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidAuthException("Email is already taken.");
        } catch (Exception e) {
            throw new InvalidAuthException("An error occurred during registration: " + e.getMessage());
        }
    }

    public String authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new InvalidAuthException("Invalid email or password");
        } catch (DisabledException e) {
            throw new InvalidAuthException("Account is disabled");
        } catch (LockedException e) {
            throw new InvalidAuthException("Account is locked");
        } catch (Exception e) {
            throw new InvalidAuthException("Authentication failed: " + e.getMessage());
        }

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(InvalidAuthException::new);
        if (!user.isEnabled()) {
            throw new InvalidAuthException("Account is disabled");
        }

        return jwtService.generateToken(user);
    }
}
