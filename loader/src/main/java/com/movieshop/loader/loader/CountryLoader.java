package com.movieshop.loader.loader;

import com.movieshop.loader.csv.CountryCsv;
import com.movieshop.loader.utils.CsvReaderUtil;
import com.movieshop.loader.domain.Country;
import com.movieshop.loader.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CountryLoader {

    private final CountryRepository countryRepository;

    public void load() {
        if (countryRepository.count() > 0) {
            return;
        }

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data/country.csv");
        if (inputStream == null) {
            throw new IllegalArgumentException("CSV file not found on classpath: data/country.com.movieshop.loader.csv");
        }
        List<CountryCsv> countryCsvList = CsvReaderUtil.readCsv(inputStream, CountryCsv.class);

        List<Country> countries = new ArrayList<>();

        for(CountryCsv countryCsv: countryCsvList){
            Country country = new Country();
            country.setName(countryCsv.getCountry());
            countries.add(country);
        }

        countryRepository.saveAll(countries);
    }
}
