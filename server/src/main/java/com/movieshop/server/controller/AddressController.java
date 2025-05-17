package com.movieshop.server.controller;

import com.movieshop.server.domain.Address;
import com.movieshop.server.mapper.AddressMapper;
import com.movieshop.server.model.AddressDTO;
import com.movieshop.server.service.IAddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v0/addresses")
public class AddressController {

    private final IAddressService addressService;
    private final AddressMapper addressMapper;

    public AddressController(IAddressService addressService, AddressMapper addressMapper) {
        this.addressService = addressService;
        this.addressMapper = addressMapper;
    }

    @GetMapping
    public ResponseEntity<List<AddressDTO>> getAllAddresses() {
        List<Address> addresses = addressService.getAllAddresses();
        if (addresses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<AddressDTO> addressDTOs = addresses.stream().map(addressMapper::toDto).toList();
        return ResponseEntity.ok(addressDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Integer id) {
        Address address = addressService.getAddressById(id);
        if (address == null) {
            return ResponseEntity.notFound().build();
        }

        AddressDTO addressDTO = addressMapper.toDto(address);
        return ResponseEntity.ok(addressDTO);
    }

    @PostMapping
    public ResponseEntity<AddressDTO> createAddress(@RequestBody AddressDTO addressDTO) {
        Address createdAddress = addressService.createAddress(addressDTO);
        URI location = URI.create("/api/v0/addresses/" + createdAddress.getId());
        return ResponseEntity.created(location)
                .body(addressMapper.toDto(createdAddress));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Integer id, @RequestBody AddressDTO addressDTO) {
        Address updatedAddress = addressService.updateAddress(id, addressDTO);
        if (updatedAddress == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(addressMapper.toDto(updatedAddress));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Integer id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}
