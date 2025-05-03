package com.movieshop.server.model;

public class SimpleErrorResponse {
    private String message;

    public SimpleErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
