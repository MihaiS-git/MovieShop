package com.movieshop.server.service;

import com.movieshop.server.domain.RefreshToken;
import com.movieshop.server.domain.User;

import java.util.Optional;

public interface IRefreshTokenService {

    RefreshToken createRefreshToken(User user);

    public Optional<RefreshToken> findByToken(String token);

    public void deleteRefreshToken(String token);

    public boolean validateRefreshToken(String token);

    public void cleanExpiredTokens();
}
