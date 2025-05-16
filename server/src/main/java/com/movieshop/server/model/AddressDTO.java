package com.movieshop.server.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AddressDTO {

    private Integer id;

    @NotNull
    @Size(min = 2, max = 50)
    private String address;

    @NotNull
    @Size(min = 2, max = 50)
    private String address2;

    @Size(min = 2, max = 50)
    private String district;

    @NotNull
    @Size(min = 2, max = 50)
    private String city;

    @Size(min = 2, max = 50)
    private String postalCode;

    @Size(min = 2, max = 50)
    private String phone;
}
