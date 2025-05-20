package com.movieshop.server.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CountryRequestDTO {

    @NotNull
    @Size(min = 2, max = 50)
    private String name;

}
