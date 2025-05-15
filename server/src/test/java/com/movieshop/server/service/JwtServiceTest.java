package com.movieshop.server.service;

import com.movieshop.server.exception.InvalidAuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    private JwtService jwtService;
    private final String secret = Base64.getEncoder().encodeToString("MySuperSecretKeyMySuperSecretKey".getBytes()); // must be 256-bit base64 for HS256

    @BeforeEach
    void setUp() throws Exception {
        jwtService = new JwtService();

        // Inject secretKey via reflection (since @Value won't work in unit test without Spring context)
        Field secretField = JwtService.class.getDeclaredField("secretKey");
        secretField.setAccessible(true);
        secretField.set(jwtService, secret);

        Field expField = JwtService.class.getDeclaredField("jwtTokenExpiration");
        expField.setAccessible(true);
        // 1 hour
        long expiration = 1000 * 60 * 60;
        expField.set(jwtService, expiration);
    }

    @Test
    void generateTokenAndExtractClaims_success() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("user@example.com");

        GrantedAuthority authority = () -> "ADMIN";
        Collection<GrantedAuthority> authorities = Collections.singleton(authority);

        // Use doReturn to bypass generics type-check issues
        doReturn(authorities).when(userDetails).getAuthorities();

        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);

        String username = jwtService.extractUsername(token);
        assertEquals("user@example.com", username);

        String email = jwtService.extractClaim(token, claims -> claims.get("email", String.class));
        assertEquals("user@example.com", email);

        String role = jwtService.extractClaim(token, claims -> claims.get("role", String.class));
        assertEquals("ADMIN", role);

        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void extractClaim_invalidToken_throwsInvalidAuthException() {
        String invalidToken = "invalid.token.string";

        assertThrows(InvalidAuthException.class, () -> jwtService.extractClaim(invalidToken, Claims::getSubject));
    }

    @Test
    void isTokenValid_expiredToken_returnsFalse() {
        UserDetails userDetails = mock(UserDetails.class);
        GrantedAuthority authority = () -> "USER";
        when(userDetails.getUsername()).thenReturn("expired@example.com");
        doReturn(Collections.singleton(authority)).when(userDetails).getAuthorities();

        // Create token with expiration in the past
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "USER");
        claims.put("email", "expired@example.com");

        // Manually build expired token
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() - 10000); // 10 seconds ago

        String expiredToken = Jwts.builder().claims(claims)
                .subject("expired@example.com")
                .issuedAt(new Date(now.getTime() - 60000))
                .expiration(expiredDate)
                .signWith(getSecretKey())
                .compact();

        assertFalse(jwtService.isTokenValid(expiredToken, userDetails));
    }

    @Test
    void isTokenExpired_validAndExpiredTokens() {
        // Valid token (not expired)
        Date future = new Date(System.currentTimeMillis() + 10000);
        String token = createTokenWithExpiration(future);
        assertFalse(jwtService.isTokenExpired(token));

        // Expired token
        Date past = new Date(System.currentTimeMillis() - 10000);
        String expiredToken = createTokenWithExpiration(past);

        // Expect your extractClaim or isTokenExpired to throw InvalidAuthException
        assertThrows(InvalidAuthException.class, () -> {
            jwtService.isTokenExpired(expiredToken);
        });
    }

    @Test
    void isTokenValid_returnsFalse_whenUsernameDoesNotMatch() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("wrongUser");

        // Spy on jwtService to mock extractUsername and isTokenExpired
        JwtService spyJwtService = spy(jwtService);
        doReturn("actualUser").when(spyJwtService).extractUsername(anyString());
        doReturn(false).when(spyJwtService).isTokenExpired(anyString());

        boolean valid = spyJwtService.isTokenValid("dummyToken", userDetails);
        assertFalse(valid);
    }

    @Test
    void isTokenValid_returnsFalse_whenTokenIsExpired() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("user");

        JwtService spyJwtService = spy(jwtService);
        doReturn("user").when(spyJwtService).extractUsername(anyString());
        doReturn(true).when(spyJwtService).isTokenExpired(anyString());

        boolean valid = spyJwtService.isTokenValid("dummyToken", userDetails);
        assertFalse(valid);
    }

    @Test
    void isTokenValid_returnsFalse_whenExtractUsernameThrowsException() {
        UserDetails userDetails = mock(UserDetails.class);

        JwtService spyJwtService = spy(jwtService);
        doThrow(new InvalidAuthException("Invalid token")).when(spyJwtService).extractUsername(anyString());

        boolean valid = spyJwtService.isTokenValid("invalidToken", userDetails);
        assertFalse(valid);
    }


    @Test
    void extractRoleFromToken_returnsCorrectRole() {
        UserDetails userDetails = mock(UserDetails.class);
        GrantedAuthority authority = () -> "ADMIN";
        when(userDetails.getUsername()).thenReturn("user@example.com");

        doReturn(Collections.singleton(authority)).when(userDetails).getAuthorities();

        String token = jwtService.generateToken(userDetails);
        String role = jwtService.extractRoleFromToken(token);

        assertEquals("ADMIN", role);
    }

    @Test
    void extractEmailFromToken_returnsCorrectEmail() {
        // Arrange
        String expectedEmail = "user@example.com";
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", expectedEmail);

        String token = Jwts.builder()
                .claims(claims)
                .subject("user@example.com")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour valid
                .signWith(getSecretKey())
                .compact();

        // Act
        String actualEmail = jwtService.extractEmailFromToken(token);

        // Assert
        assertEquals(expectedEmail, actualEmail);
    }

    private String createTokenWithExpiration(Date expirationDate) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "USER");
        claims.put("email", "test@example.com");

        return Jwts.builder().claims(claims)
                .subject("test@example.com")
                .issuedAt(new Date())
                .expiration(expirationDate)
                .signWith(getSecretKey())
                .compact();
    }

    private SecretKey getSecretKey() {
        return io.jsonwebtoken.security.Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }
}
