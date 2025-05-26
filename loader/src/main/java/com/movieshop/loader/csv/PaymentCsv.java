package com.movieshop.loader.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentCsv {

    @CsvBindByName(column = "payment_id")
    private String paymentId;

    @CsvBindByName(column = "customer_id")
    private String customerId;

    @CsvBindByName(column = "staff_id")
    private String staffId;

    @CsvBindByName(column = "rental_id")
    private String rentalId;

    @CsvBindByName(column = "amount")
    private String amount;

    @CsvBindByName(column = "payment_date")
    private String paymentDate;

}
