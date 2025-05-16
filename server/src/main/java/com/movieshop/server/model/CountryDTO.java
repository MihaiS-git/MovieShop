package com.movieshop.server.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CountryDTO {

    private Integer id;

    @NotNull
    @Size(min = 2, max = 50)
    private String name;

    @Builder.Default
    private List<Integer> cities = new ArrayList<>();
}
