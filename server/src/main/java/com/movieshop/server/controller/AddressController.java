package com.movieshop.server.controller;

import com.movieshop.server.model.AddressRequestDTO;
import com.movieshop.server.model.AddressResponseDTO;
import com.movieshop.server.service.IAddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v0/addresses")
public class AddressController {

    private final IAddressService addressService;

    public AddressController(IAddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public ResponseEntity<List<AddressResponseDTO>> getAllAddresses() {
        List<AddressResponseDTO> addresses = addressService.getAllAddresses();
        if (addresses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> getAddressById(@PathVariable Integer id) {
        AddressResponseDTO address = addressService.getAddressById(id);
        if (address == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(address);
    }

    @PostMapping
    public ResponseEntity<AddressResponseDTO> createAddress(@RequestBody AddressRequestDTO addressRequestDTO) {
        AddressResponseDTO createdAddress = addressService.createAddress(addressRequestDTO);
        URI location = URI.create("/api/v0/addresses/" + createdAddress.getId());
        return ResponseEntity.created(location).body(createdAddress);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> updateAddress(@PathVariable Integer id, @RequestBody AddressRequestDTO addressRequestDTO) {
        AddressResponseDTO updatedAddress = addressService.updateAddress(id, addressRequestDTO);
        if (updatedAddress == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedAddress);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Integer id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}
