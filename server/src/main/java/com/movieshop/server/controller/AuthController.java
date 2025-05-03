package com.movieshop.server.controller;

import com.movieshop.server.domain.User;
import com.movieshop.server.exception.InvalidAuthException;
import com.movieshop.server.model.*;
import com.movieshop.server.service.AuthenticationService;
import com.movieshop.server.service.IUSerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v0/auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final IUSerService userService;

    public AuthController(AuthenticationService authenticationService, IUSerService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        try {
            String token = authenticationService.register(request);
            return ResponseEntity.ok(token);
        } catch (InvalidAuthException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid registration details: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            String jwtToken = authenticationService.authenticate(authenticationRequest);

            User user = userService.getUserByEmail(authenticationRequest.getEmail());

            UserDTO userDTO = UserDTO.builder()
                    .email(user.getEmail())
                    .name(user.getName())
                    .role(user.getRole().name())
                    .picture(user.getPicture())
                    .build();

            return ResponseEntity.ok(new AuthResponse(jwtToken, userDTO));
        } catch (InvalidAuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new SimpleErrorResponse("Invalid credentials: " + e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal Object principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (principal instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) principal;
            UserDTO userDto = UserDTO.builder()
                    .email(oAuth2User.getAttribute("email"))
                    .name(oAuth2User.getAttribute("name"))
                    .role("ROLE_USER")
                    .picture(oAuth2User.getAttribute("picture"))
                    .build();

            return ResponseEntity.ok(userDto);
        }

        if (principal instanceof User) {
            User user = (User) principal;
            UserDTO userDto = UserDTO.builder()
                    .email(user.getEmail())
                    .name(user.getName())
                    .role(user.getRole().name())
                    .picture(user.getPicture())
                    .build();

            return ResponseEntity.ok(userDto);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
