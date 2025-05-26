package com.movieshop.loader.loader;

import com.movieshop.loader.csv.CustomerCsv;
import com.movieshop.loader.utils.CsvReaderUtil;
import com.movieshop.loader.utils.ParsingUtils;
import com.movieshop.loader.domain.Role;
import com.movieshop.loader.domain.User;
import com.movieshop.loader.repository.AddressRepository;
import com.movieshop.loader.repository.StoreRepository;
import com.movieshop.loader.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomerLoader {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    public void load(){
        if(userRepository.count() > 1500){
            return;
        }

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data/customer.csv");
        if (inputStream == null) {
            throw new IllegalArgumentException("CSV file not found on classpath: data/customer.com.movieshop.loader.csv");
        }
        List<CustomerCsv> customerCsvList = CsvReaderUtil.readCsv(inputStream, CustomerCsv.class);

        List<User> users = new ArrayList<>();

        for(CustomerCsv customerCsv : customerCsvList) {
            User user = new User();

            user.setStore(
                    ParsingUtils.safeFindById(
                            ParsingUtils.parseIntSafe(customerCsv.getStoreId()),
                            storeRepository,
                            "Store not found with id: " + customerCsv.getStoreId()
                    )
            );

            user.setFirstName(customerCsv.getFirstName());
            user.setLastName(customerCsv.getLastName());
            user.setEmail(customerCsv.getEmail());

            user.setAddress(
                    ParsingUtils.safeFindById(
                            ParsingUtils.parseIntSafe(customerCsv.getAddressId()),
                            addressRepository,
                            "Address not found with id: " + customerCsv.getAddressId()
                    )
            );

            user.setPassword(passwordEncoder.encode("password"));
            user.setRole(Role.CUSTOMER);

            users.add(user);
        }
        userRepository.saveAll(users);
    }
}
