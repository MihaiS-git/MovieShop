package com.movieshop.server.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LanguageDTO {
    private Integer id;
    private String name;

    public LanguageDTO(String name) {
        this.name = name;
    }
}
