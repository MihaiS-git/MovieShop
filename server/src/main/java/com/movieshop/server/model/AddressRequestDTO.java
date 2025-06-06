package com.movieshop.server.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressRequestDTO {

    @NotNull
    @Size(min = 2, max = 50)
    private String address;

    @Size(min = 0, max = 50)
    private String address2;

    @NotNull
    @Size(min = 2, max = 50)
    private String district;

    @NotNull
    @Size(min = 2, max = 50)
    private String city;

    @NotNull
    @Size(min = 2, max = 50)
    private String country;

    @NotNull
    @Size(min = 6, max = 10)
    private String postalCode;

    @NotNull
    @Size(min = 10, max = 16)
    private String phone;

    @NotNull
    private Integer userId;
}
