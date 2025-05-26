package com.movieshop.loader.loader;

import com.movieshop.loader.csv.AddressCsv;
import com.movieshop.loader.utils.CsvReaderUtil;
import com.movieshop.loader.utils.ParsingUtils;
import com.movieshop.loader.domain.Address;
import com.movieshop.loader.repository.AddressRepository;
import com.movieshop.loader.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AddressLoader {

    private final AddressRepository addressRepository;
    private final CityRepository cityRepository;

    public void load(){
        if(addressRepository.count() > 0){
            return;
        }

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data/address.csv");
        if (inputStream == null) {
            throw new IllegalArgumentException("CSV file not found on classpath: data/address.com.movieshop.loader.csv");
        }
        List<AddressCsv> addressCsvList = CsvReaderUtil.readCsv(inputStream, AddressCsv.class);

        List<Address> addresses = new ArrayList<>();

        for(AddressCsv addressCsv: addressCsvList){
            Address address = new Address();
            address.setAddress(addressCsv.getAddress());
            address.setAddress2(addressCsv.getAddress2());
            address.setDistrict(addressCsv.getDistrict());

            address.setCity(
                    ParsingUtils.safeFindById(
                            ParsingUtils.parseIntSafe(addressCsv.getCityId()),
                            cityRepository,
                            "City not found with id: " + addressCsv.getCityId()
                    )
            );

            address.setPostalCode(addressCsv.getPostalCode());
            address.setPhone(addressCsv.getPhone());

            addresses.add(address);
        }

        addressRepository.saveAll(addresses);
    }
}
