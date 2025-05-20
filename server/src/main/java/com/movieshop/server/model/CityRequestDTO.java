package com.movieshop.server.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CityRequestDTO {

    @NotNull
    @Size(min = 2, max = 50)
    private String name;

    @NotNull
    @Size(min = 2, max = 50)
    private String country;
}
