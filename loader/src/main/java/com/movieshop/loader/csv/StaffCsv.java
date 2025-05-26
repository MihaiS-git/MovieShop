package com.movieshop.loader.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaffCsv {

    @CsvBindByName(column = "first_name")
    private String firstName;

    @CsvBindByName(column = "last_name")
    private String lastName;

    @CsvBindByName(column = "address_id")
    private String addressId;

    @CsvBindByName(column = "email")
    private String email;

    @CsvBindByName(column = "store_id")
    private String storeId;

    @CsvBindByName(column = "password")
    private String password;

}
