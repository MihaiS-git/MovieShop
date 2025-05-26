package com.movieshop.loader.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressCsv {

    @CsvBindByName(column = "address")
    private String address;

    @CsvBindByName(column = "address2")
    private String address2;

    @CsvBindByName(column = "district")
    private String district;

    @CsvBindByName(column = "city_id")
    private String cityId;

    @CsvBindByName(column = "postal_code")
    private String postalCode;

    @CsvBindByName(column = "phone")
    private String phone;

}
