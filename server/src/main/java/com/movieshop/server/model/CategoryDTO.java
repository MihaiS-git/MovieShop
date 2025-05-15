package com.movieshop.server.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {
    private Integer id;
    private String name;

    public CategoryDTO(String newName) {
        this.name = newName;
    }
}
