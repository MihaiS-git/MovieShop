package com.movieshop.server.service;

import com.movieshop.server.domain.Role;
import com.movieshop.server.domain.User;
import com.movieshop.server.exception.InvalidAuthException;
import com.movieshop.server.model.AuthenticationRequest;
import com.movieshop.server.model.RegisterRequest;
import com.movieshop.server.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
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
        log.info("User {} is attempting to register.", request.getEmail());
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

            log.info("User {} registered successfully with role {}", savedUser.getEmail(), savedUser.getRole());

            return jwtService.generateToken(savedUser);
        } catch (Exception e) {
            log.error("Error during registration: {}", e.getMessage());
            throw new InvalidAuthException("An error occurred during registration: " + e.getMessage());
        }
    }

    public String authenticate(AuthenticationRequest request) {
        log.info("User {} is attempting to authenticate.", request.getEmail());
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new InvalidAuthException("Invalid email or password");
        } catch (DisabledException e) {
            throw new InvalidAuthException("Account is disabled");
        } catch (LockedException e) {
            throw new InvalidAuthException("Account is locked");
        } catch (Exception e) {
            log.error("Authentication error: {}", e.getMessage());
            throw new InvalidAuthException("Authentication failed: " + e.getMessage());
        }

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new InvalidAuthException("Invalid email or password"));
        if (!user.isEnabled()) {
            throw new InvalidAuthException("Account is disabled");
        }

        log.info("User {} authenticated successfully with role {}", user.getEmail(), user.getRole());

        return jwtService.generateToken(user);
    }
}
