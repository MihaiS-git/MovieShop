package com.movieshop.server.service;

import com.movieshop.server.domain.RefreshToken;
import com.movieshop.server.domain.User;
import com.movieshop.server.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RefreshTokenServiceImplTest {

    private RefreshTokenRepository refreshTokenRepository;
    private RefreshTokenServiceImpl refreshTokenService;
    private User user;

    @BeforeEach
    void setUp() throws Exception {
        refreshTokenRepository = mock(RefreshTokenRepository.class);
        refreshTokenService = new RefreshTokenServiceImpl(refreshTokenRepository);

        // Use reflection to set private field refreshTokenExpirationTime
        Field field = RefreshTokenServiceImpl.class.getDeclaredField("refreshTokenExpirationTime");
        field.setAccessible(true);
        field.set(refreshTokenService, 7L);

        user = new User();
        user.setId(1);  // assuming user id is Integer, not Long
    }

    @Test
    void createRefreshToken_existingToken_updatesAndSaves() {
        RefreshToken existingToken = new RefreshToken();
        existingToken.setUser(user);
        existingToken.setToken("old-token");
        existingToken.setExpiryDate(LocalDateTime.now().plusDays(1));

        when(refreshTokenRepository.findByUserId(user.getId())).thenReturn(Optional.of(existingToken));
        when(refreshTokenRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        RefreshToken result = refreshTokenService.createRefreshToken(user);

        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertNotEquals("old-token", result.getToken()); // Token was regenerated
        assertTrue(result.getExpiryDate().isAfter(LocalDateTime.now()));

        verify(refreshTokenRepository).findByUserId(user.getId());
        verify(refreshTokenRepository).save(result);
    }

    @Test
    void createRefreshToken_noExistingToken_createsNewAndSaves() {
        when(refreshTokenRepository.findByUserId(user.getId())).thenReturn(Optional.empty());
        when(refreshTokenRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        RefreshToken result = refreshTokenService.createRefreshToken(user);

        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertNotNull(result.getToken());
        assertTrue(result.getExpiryDate().isAfter(LocalDateTime.now()));

        verify(refreshTokenRepository).findByUserId(user.getId());
        verify(refreshTokenRepository).save(result);
    }

    @Test
    void findByToken_found() {
        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);

        when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.of(refreshToken));

        Optional<RefreshToken> result = refreshTokenService.findByToken(token);

        assertTrue(result.isPresent());
        assertEquals(token, result.get().getToken());
        verify(refreshTokenRepository).findByToken(token);
    }

    @Test
    void findByToken_notFound() {
        String token = "non-existent-token";
        when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        Optional<RefreshToken> result = refreshTokenService.findByToken(token);

        assertTrue(result.isEmpty());
        verify(refreshTokenRepository).findByToken(token);
    }

    @Test
    void deleteRefreshToken_tokenExists_deletesToken() {
        String token = "token-to-delete";
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);

        when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.of(refreshToken));

        refreshTokenService.deleteRefreshToken(token);

        verify(refreshTokenRepository).findByToken(token);
        verify(refreshTokenRepository).delete(refreshToken);
    }

    @Test
    void deleteRefreshToken_tokenDoesNotExist_noDeletion() {
        String token = "non-existent-token";

        when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        refreshTokenService.deleteRefreshToken(token);

        verify(refreshTokenRepository).findByToken(token);
        verify(refreshTokenRepository, never()).delete(any());
    }

    @Test
    void validateRefreshToken_validToken_returnsTrue() {
        String token = "valid-token";
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(1));

        when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.of(refreshToken));

        assertTrue(refreshTokenService.validateRefreshToken(token));

        verify(refreshTokenRepository).findByToken(token);
    }

    @Test
    void validateRefreshToken_expiredToken_returnsFalse() {
        String token = "expired-token";
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setExpiryDate(LocalDateTime.now().minusDays(1));

        when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.of(refreshToken));

        assertFalse(refreshTokenService.validateRefreshToken(token));

        verify(refreshTokenRepository).findByToken(token);
    }

    @Test
    void validateRefreshToken_tokenNotFound_returnsFalse() {
        String token = "missing-token";

        when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        assertFalse(refreshTokenService.validateRefreshToken(token));

        verify(refreshTokenRepository).findByToken(token);
    }

    @Test
    void cleanExpiredTokens_deletesOnlyExpiredTokens() {
        RefreshToken expiredToken = new RefreshToken();
        expiredToken.setExpiryDate(LocalDateTime.now().minusDays(1));

        RefreshToken validToken = new RefreshToken();
        validToken.setExpiryDate(LocalDateTime.now().plusDays(1));

        when(refreshTokenRepository.findAll()).thenReturn(List.of(expiredToken, validToken));

        refreshTokenService.cleanExpiredTokens();

        verify(refreshTokenRepository).findAll();
        verify(refreshTokenRepository).delete(expiredToken);
        verify(refreshTokenRepository, never()).delete(validToken);
    }
}
