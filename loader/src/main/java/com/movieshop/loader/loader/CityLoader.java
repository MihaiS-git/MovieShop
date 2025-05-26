package com.movieshop.loader.loader;

import com.movieshop.loader.csv.CityCsv;
import com.movieshop.loader.utils.CsvReaderUtil;
import com.movieshop.loader.utils.ParsingUtils;
import com.movieshop.loader.domain.City;
import com.movieshop.loader.repository.CityRepository;
import com.movieshop.loader.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CityLoader {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    public void load() {
        if (cityRepository.count() > 0) {
            return;
        }

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data/city.csv");
        if (inputStream == null) {
            throw new IllegalArgumentException("CSV file not found on classpath: data/city.com.movieshop.loader.csv");
        }
        List<CityCsv> cityCsvList = CsvReaderUtil.readCsv(inputStream, CityCsv.class);

        List<City> cities = new ArrayList<>();

        for (CityCsv cityCsv : cityCsvList) {
            City city = new City();
            city.setName(cityCsv.getCity());

            city.setCountry(
                    ParsingUtils.safeFindById(
                            ParsingUtils.parseIntSafe(cityCsv.getCountry_id()),
                            countryRepository,
                            "Country not found: " + cityCsv.getCountry_id()
                    )
            );

            cities.add(city);
        }

        cityRepository.saveAll(cities);
    }
}
