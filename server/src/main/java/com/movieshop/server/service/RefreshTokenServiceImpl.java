package com.movieshop.server.service;

import com.movieshop.server.domain.RefreshToken;
import com.movieshop.server.domain.User;
import com.movieshop.server.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class RefreshTokenServiceImpl implements IRefreshTokenService{

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpirationTime;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public RefreshToken createRefreshToken(User user) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusDays(refreshTokenExpirationTime);

        // Delete any existing token first
        refreshTokenRepository.findByUserId(user.getId())
                .ifPresent(refreshTokenRepository::delete);

        RefreshToken newToken = new RefreshToken();
        newToken.setToken(token);
        newToken.setUser(user);
        newToken.setExpiryDate(expiryDate);

        return refreshTokenRepository.save(newToken);
    }


    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    @Transactional
    public void deleteRefreshToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(refreshTokenRepository::delete);
    }

    @Override
    public boolean validateRefreshToken(String token) {
        Optional<RefreshToken> refreshTokenOptional = findByToken(token);

        if (refreshTokenOptional.isEmpty()) {
            log.warn("Refresh token not found: {}", token);
            return false;
        }

        RefreshToken refreshToken = refreshTokenOptional.get();

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            log.info("Refresh token expired at {}", refreshToken.getExpiryDate());
            return false;
        }

        return refreshToken.getExpiryDate().isAfter(LocalDateTime.now());
    }

    @Override
    public void cleanExpiredTokens() {
        refreshTokenRepository.findAll().forEach(refreshToken -> {
            if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
                refreshTokenRepository.delete(refreshToken);
            }
        });
    }
}
