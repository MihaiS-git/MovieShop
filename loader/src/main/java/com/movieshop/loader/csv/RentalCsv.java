package com.movieshop.loader.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RentalCsv {

    @CsvBindByName(column = "rental_date")
    private String rentalDate;

    @CsvBindByName(column = "inventory_id")
    private String inventoryId;

    @CsvBindByName(column = "customer_id")
    private String customerId;

    @CsvBindByName(column = "return_date")
    private String returnDate;

    @CsvBindByName(column = "staff_id")
    private String staffId;

}
