package com.movieshop.loader.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryCsv {

    @CsvBindByName(column = "film_id")
    private String filmId;

    @CsvBindByName(column = "store_id")
    private String storeId;

}
