package com.movieshop.loader.service;

import com.movieshop.loader.csv.InventoryCsv;
import com.movieshop.loader.utils.CsvReaderUtil;
import com.movieshop.loader.utils.ParsingUtils;
import com.movieshop.loader.domain.Film;
import com.movieshop.loader.domain.Inventory;
import com.movieshop.loader.domain.Store;
import com.movieshop.loader.repository.FilmRepository;
import com.movieshop.loader.repository.InventoryRepository;
import com.movieshop.loader.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final FilmRepository filmRepository;
    private final StoreRepository storeRepository;
    private final InventoryRepository inventoryRepository;

    @Transactional
    public void load() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data/inventory.csv")) {
            if (inputStream == null) {
                throw new IllegalArgumentException("CSV file not found on classpath: data/inventory.com.movieshop.loader.csv");
            }

            List<InventoryCsv> inventoryCsvList = CsvReaderUtil.readCsv(inputStream, InventoryCsv.class);

            for (InventoryCsv inventoryCsv : inventoryCsvList) {
                Inventory inventory = new Inventory();

                Film film = ParsingUtils.safeFindById(
                        ParsingUtils.parseIntSafe(inventoryCsv.getFilmId()),
                        filmRepository,
                        "Film not found with id: " + inventoryCsv.getFilmId()
                );
                inventory.setFilm(film);

                Store store = ParsingUtils.safeFindById(
                        ParsingUtils.parseIntSafe(inventoryCsv.getStoreId()),
                        storeRepository,
                        "Store not found with id: " + inventoryCsv.getStoreId()
                );
                inventory.setStore(store);

                Inventory savedInventory = inventoryRepository.save(inventory);

                film.addInventory(savedInventory);
                store.addInventory(savedInventory);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load inventory data", e);
        }
    }
}
