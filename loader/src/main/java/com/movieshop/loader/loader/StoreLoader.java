package com.movieshop.loader.loader;

import com.movieshop.loader.csv.StoreCsv;
import com.movieshop.loader.utils.CsvReaderUtil;
import com.movieshop.loader.utils.ParsingUtils;
import com.movieshop.loader.domain.Store;
import com.movieshop.loader.domain.User;
import com.movieshop.loader.repository.AddressRepository;
import com.movieshop.loader.repository.StoreRepository;
import com.movieshop.loader.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StoreLoader {

    private final StoreRepository storeRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public void load(){
        if(storeRepository.count() > 0){
            return;
        }

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data/store.csv");
        if (inputStream == null) {
            throw new IllegalArgumentException("CSV file not found on classpath: data/store.com.movieshop.loader.csv");
        }
        List<StoreCsv> storeCsvList = CsvReaderUtil.readCsv(inputStream, StoreCsv.class);

        List<Store> stores = new ArrayList<>();

        for(StoreCsv storeCsv: storeCsvList){
            Store store = new Store();

            store.setAddress(
                    ParsingUtils.safeFindById(
                            ParsingUtils.parseIntSafe(storeCsv.getAddressId()),
                            addressRepository,
                            "Address not found with id: " + storeCsv.getAddressId()
                    )
            );

            stores.add(store);
        }

        storeRepository.saveAll(stores);

        for(int i = 0; i < storeCsvList.size(); i++){
            Store store = stores.get(i);
            StoreCsv storeCsv = storeCsvList.get(i);

            User manager = ParsingUtils.safeFindById(
                    ParsingUtils.parseIntSafe(storeCsv.getManagerStaffId()) + 1,
                    userRepository,
                    "Staff not found with id: " + storeCsv.getManagerStaffId()
            );
            store.setManagerStaff(manager);

            manager.addStore(store);
            userRepository.save(manager);
        }

        storeRepository.saveAll(stores);
    }

}
