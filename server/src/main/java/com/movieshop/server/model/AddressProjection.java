package com.movieshop.server.model;

import java.time.OffsetDateTime;

public interface AddressProjection {
    Integer getId();
    String getAddress();
    String getAddress2();
    String getDistrict();
    String getPhone();
    String getPostalCode();
    CityInfo getCity();
    OffsetDateTime getLastUpdate();

    interface CityInfo{
        Integer getId();
        String getName();
        CountryInfo getCountry();
        OffsetDateTime getLastUpdate();

        interface CountryInfo{
            Integer getId();
            String getName();
            OffsetDateTime getLastUpdate();
        }
    }
}
