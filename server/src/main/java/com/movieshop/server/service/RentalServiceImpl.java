package com.movieshop.server.service;

import com.movieshop.server.domain.Inventory;
import com.movieshop.server.domain.Rental;
import com.movieshop.server.domain.User;
import com.movieshop.server.exception.ResourceNotFoundException;
import com.movieshop.server.mapper.RentalMapper;
import com.movieshop.server.model.RentalRequestDTO;
import com.movieshop.server.model.RentalResponseDTO;
import com.movieshop.server.repository.InventoryRepository;
import com.movieshop.server.repository.RentalRepository;
import com.movieshop.server.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentalServiceImpl implements IRentalService{

    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;


    public RentalServiceImpl(RentalRepository rentalRepository, RentalMapper rentalMapper, InventoryRepository inventoryRepository, UserRepository userRepository) {
        this.rentalRepository = rentalRepository;
        this.rentalMapper = rentalMapper;
        this.inventoryRepository = inventoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public RentalResponseDTO createRental(RentalRequestDTO rentalRequestDTO) {
        Rental rental = rentalMapper.toEntity(rentalRequestDTO);
        Inventory inventory = inventoryRepository.findById(rentalRequestDTO.getInventoryId()).orElseThrow(() ->
                new ResourceNotFoundException("Inventory not found with id: " + rentalRequestDTO.getInventoryId()));
        User customer = userRepository.findById(rentalRequestDTO.getCustomerId()).orElseThrow(() ->
                new ResourceNotFoundException("Customer not found with id: " + rentalRequestDTO.getCustomerId()));
        User staff = userRepository.findById(rentalRequestDTO.getStaffId()).orElseThrow(() ->
                new ResourceNotFoundException("Staff not found with id: " + rentalRequestDTO.getStaffId()));

        inventory.addRental(rental);
        customer.addRental(rental);
        staff.addRental(rental);

        rental = rentalRepository.save(rental);

        return rentalMapper.toResponseDto(rental);
    }

    @Override
    public RentalResponseDTO getRentalById(Integer rentalId) {
        Rental rental = rentalRepository.findById(rentalId).orElseThrow(() ->
                new ResourceNotFoundException("Rental not found with id: " + rentalId));
        return rentalMapper.toResponseDto(rental);
    }

    @Override
    public List<RentalResponseDTO> getAllRentals() {
        List<Rental> rentals = rentalRepository.findAll();
        return rentals.stream().map(rentalMapper::toResponseDto).toList();
    }

    @Override
    public RentalResponseDTO updateRental(Integer rentalId, RentalRequestDTO rentalRequestDTO) {
        Rental existentRental = rentalRepository.findById(rentalId).orElseThrow(() ->
                new ResourceNotFoundException("Rental not found with id: " + rentalId));

        // Remove from old associations if needed
        Inventory oldInventory = existentRental.getInventory();
        if (oldInventory != null && !oldInventory.getId().equals(rentalRequestDTO.getInventoryId())) {
            oldInventory.removeRental(existentRental);
        }

        User oldCustomer = existentRental.getCustomer();
        if (oldCustomer != null && !oldCustomer.getId().equals(rentalRequestDTO.getCustomerId())) {
            oldCustomer.removeRental(existentRental);
        }

        User oldStaff = existentRental.getStaff();
        if (oldStaff != null && !oldStaff.getId().equals(rentalRequestDTO.getStaffId())) {
            oldStaff.removeRental(existentRental);
        }

        // Update rental fields
        existentRental.setRentalDate(rentalRequestDTO.getRentalDate());
        existentRental.setRentalPeriod(rentalRequestDTO.getRentalPeriod());

        // Fetch new references
        Inventory newInventory = inventoryRepository.findById(rentalRequestDTO.getInventoryId()).orElseThrow(() ->
                new ResourceNotFoundException("Inventory not found with id: " + rentalRequestDTO.getInventoryId()));
        User newCustomer = userRepository.findById(rentalRequestDTO.getCustomerId()).orElseThrow(() ->
                new ResourceNotFoundException("Customer not found with id: " + rentalRequestDTO.getCustomerId()));
        User newStaff = userRepository.findById(rentalRequestDTO.getStaffId()).orElseThrow(() ->
                new ResourceNotFoundException("Staff not found with id: " + rentalRequestDTO.getStaffId()));

        // Set new associations
        existentRental.setInventory(newInventory);
        existentRental.setCustomer(newCustomer);
        existentRental.setStaff(newStaff);

        // Add to new entities
        newInventory.addRental(existentRental);
        newCustomer.addRental(existentRental);
        newStaff.addRental(existentRental);

        existentRental = rentalRepository.save(existentRental);
        return rentalMapper.toResponseDto(existentRental);
    }


    @Override
    public void deleteRental(Integer rentalId) {
        Rental existentRental = rentalRepository.findById(rentalId).orElseThrow(() ->
                new ResourceNotFoundException("Rental not found with id: " + rentalId));
        rentalRepository.delete(existentRental);
    }
}
