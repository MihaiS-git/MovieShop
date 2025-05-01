package com.movieshop.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.UNAUTHORIZED)
public class InvalidAuthException extends RuntimeException{
    public InvalidAuthException() {
        super("Invalid email or password");
    }

    public InvalidAuthException(String message) {
        super(message);
    }
}
