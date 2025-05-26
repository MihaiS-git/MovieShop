package com.movieshop.loader;

import com.movieshop.loader.loader.*;
import com.movieshop.loader.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ActorLoader actorLoader;
    private final LanguageLoader languageLoader;
    private final FilmLoader filmLoader;
    private final CategoryLoader categoryLoader;
    private final CountryLoader countryLoader;
    private final CityLoader cityLoader;
    private final AddressLoader addressLoader;
    private final StaffLoader staffLoader;
    private final StoreLoader storeLoader;
    private final CustomerLoader customerLoader;
    private final RentalService rentalService;

    private final FilmCategoryService filmCategoryService;
    private final ActorFilmService actorFilmService;
    private final InventoryService inventoryService;
    private final PaymentService paymentService;

    @Override
    public void run(String... args) throws Exception {
        log.info("Loading initial data...");
        actorLoader.load();
        languageLoader.load();
        categoryLoader.load();
        filmLoader.load();
        countryLoader.load();
        cityLoader.load();
        addressLoader.load();
        staffLoader.load();
        storeLoader.load();
        customerLoader.load();
        inventoryService.load();
        rentalService.load();
        paymentService.load();

        filmCategoryService.loadFilmCategoryData();
        actorFilmService.loadActorFilmData();

        log.info("Initial data successfully loaded.");
    }
}
