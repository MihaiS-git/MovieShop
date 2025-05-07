package com.movieshop.server.security;

import com.movieshop.server.domain.RefreshToken;
import com.movieshop.server.domain.Role;
import com.movieshop.server.domain.User;
import com.movieshop.server.repository.UserRepository;
import com.movieshop.server.service.IRefreshTokenService;
import com.movieshop.server.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Value("${frontend.base-url}")
    private String FRONTEND_BASE_URL;

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final IRefreshTokenService refreshTokenService;

    public CustomOAuth2SuccessHandler(JwtService jwtService, UserRepository userRepository, IRefreshTokenService refreshTokenService) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        DefaultOAuth2User oauthUser = (DefaultOAuth2User) authentication.getPrincipal();

        String email = Optional.ofNullable(oauthUser.getAttribute("email"))
                .map(String.class::cast)
                .orElseThrow(() -> new IllegalArgumentException("Email not found in OAuth2 response"));

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setPassword(UUID.randomUUID().toString());
            newUser.setName(oauthUser.getAttribute("name"));
            newUser.setPicture(oauthUser.getAttribute("picture"));
            newUser.setRole(Role.CUSTOMER);
            newUser.setAccountNonExpired(true);
            newUser.setAccountNonLocked(true);
            newUser.setCredentialsNonExpired(true);
            newUser.setEnabled(true);
            return userRepository.save(newUser);
        });

        String jwtToken = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        String redirectUri = UriComponentsBuilder.fromUriString(FRONTEND_BASE_URL + "/oauth2/redirect")
                .queryParam("token", jwtToken)
                .queryParam("refreshToken", refreshToken.getToken())
                .build()
                .toUriString();
        response.sendRedirect(redirectUri);
    }
}
