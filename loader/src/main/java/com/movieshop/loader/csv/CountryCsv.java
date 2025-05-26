package com.movieshop.loader.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountryCsv {

    @CsvBindByName(column = "country")
    private String country;
}
