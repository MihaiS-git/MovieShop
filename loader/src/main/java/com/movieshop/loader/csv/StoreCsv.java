package com.movieshop.loader.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreCsv {

    @CsvBindByName(column = "manager_staff_id")
    private String managerStaffId;

    @CsvBindByName(column = "address_id")
    private String addressId;

}
