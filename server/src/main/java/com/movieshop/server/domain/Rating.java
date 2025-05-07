package com.movieshop.server.domain;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@ToString
public enum Rating {
    G,
    PG,
    PG13("PG-13"),
    R,
    NC17("NC-17");

    private final String displayName;

    Rating() {
        this.displayName = name();
    }

    Rating(String displayName) {
        this.displayName = displayName;
    }

    public static Rating fromString(String s) {
        for (Rating rating : Rating.values()) {
            if (rating.displayName.equalsIgnoreCase(s)) {
                return rating;
            }
        }
        throw new IllegalArgumentException("Unknown rating: " + s);
    }
}
