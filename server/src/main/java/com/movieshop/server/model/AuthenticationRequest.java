package com.movieshop.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AuthenticationRequest {
    private String email;
    String password;
}
