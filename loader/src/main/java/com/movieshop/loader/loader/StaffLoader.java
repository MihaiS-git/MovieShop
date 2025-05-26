package com.movieshop.loader.loader;

import com.movieshop.loader.csv.StaffCsv;
import com.movieshop.loader.utils.CsvReaderUtil;
import com.movieshop.loader.utils.ParsingUtils;
import com.movieshop.loader.domain.Role;
import com.movieshop.loader.domain.User;
import com.movieshop.loader.repository.AddressRepository;
import com.movieshop.loader.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StaffLoader {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    public void load() {
        if (userRepository.count() > 0) {
            return;
        }

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data/staff.csv");
        if (inputStream == null) {
            throw new IllegalArgumentException("CSV file not found on classpath: data/staff.com.movieshop.loader.csv");
        }
        List<StaffCsv> staffCsvList = CsvReaderUtil.readCsv(inputStream, StaffCsv.class);

        List<User> staff = new ArrayList<>();

        for (StaffCsv staffCsv : staffCsvList) {
            User user = new User();
            user.setFirstName(staffCsv.getFirstName());
            user.setLastName(staffCsv.getLastName());

            user.setAddress(
                    ParsingUtils.safeFindById(
                            ParsingUtils.parseIntSafe(staffCsv.getAddressId()),
                            addressRepository,
                            "Address not found with id: " + staffCsv.getAddressId()
                    )
            );

            user.setEmail(staffCsv.getEmail());

            user.setPassword(passwordEncoder.encode(staffCsv.getPassword()));
            user.setRole(Role.valueOf("STAFF"));

            staff.add(user);
        }

        userRepository.saveAll(staff);
    }

}
