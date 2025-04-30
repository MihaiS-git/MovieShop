package com.movieshop.server.model;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class AuthenticationResponse {

    private String token;

}
