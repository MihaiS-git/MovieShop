package com.movieshop.server.controller;

import com.movieshop.server.domain.RefreshToken;
import com.movieshop.server.domain.User;
import com.movieshop.server.exception.InvalidAuthException;
import com.movieshop.server.model.*;
import com.movieshop.server.service.AuthenticationService;
import com.movieshop.server.service.IRefreshTokenService;
import com.movieshop.server.service.IUserService;
import com.movieshop.server.service.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v0/auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final IUserService userService;
    private final JwtService jwtService;
    private final IRefreshTokenService refreshTokenService;

    public AuthController(AuthenticationService authenticationService,
                          IUserService userService,
                          JwtService jwtService,
                          IRefreshTokenService refreshTokenService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        String token = authenticationService.register(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
        String jwtToken = authenticationService.authenticate(authenticationRequest);

        User user = userService.getUserByEmail(authenticationRequest.getEmail());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        UserDTO userDTO = UserDTO.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .picture(user.getPicture())
                .build();

        return ResponseEntity.ok(new AuthResponse(jwtToken, refreshToken.getToken(), userDTO));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal Object principal) {
        if (principal == null) {
            throw new InvalidAuthException("User not authenticated");
        }

        UserDTO userDTO = switch (principal) {
            case OAuth2User oAuth2User -> UserDTO.builder()
                    .email(oAuth2User.getAttribute("email"))
                    .firstName(oAuth2User.getAttribute("name"))
                    .lastName(oAuth2User.getAttribute("family_name"))
                    .role("ROLE_USER")
                    .picture(oAuth2User.getAttribute("picture"))
                    .build();
            case User user -> UserDTO.builder()
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .role(user.getRole().name())
                    .picture(user.getPicture())
                    .build();
            default -> throw new InvalidAuthException("Unsupported authentication type");
        };

        return ResponseEntity.ok(userDTO);
    }

    @Transactional
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        if (!refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken())) {
            throw new InvalidAuthException("Invalid or expired refresh token");
        }

        RefreshToken oldToken = refreshTokenService.findByToken(refreshTokenRequest.getRefreshToken())
                .orElseThrow(() -> new InvalidAuthException("Refresh token not found"));

        User user = oldToken.getUser();

        // Delete the old token
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());

        // Generate new token
        String newAccessToken = jwtService.generateToken(user);
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);

        UserDTO userDTO = UserDTO.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .picture(user.getPicture())
                .build();

        return ResponseEntity.ok(new AuthResponse(newAccessToken, newRefreshToken.getToken(), userDTO));
    }

}
