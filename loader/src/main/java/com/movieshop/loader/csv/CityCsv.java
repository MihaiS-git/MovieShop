package com.movieshop.loader.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CityCsv {

    @CsvBindByName(column = "city")
    private String city;

    @CsvBindByName(column = "country_id")
    private String country_id;

}
