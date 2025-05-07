package com.movieshop.server.model;

import com.movieshop.server.domain.RefreshToken;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String refreshToken;
    private UserDTO user;
}
