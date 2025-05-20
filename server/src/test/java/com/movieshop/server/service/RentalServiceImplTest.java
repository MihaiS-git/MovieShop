package com.movieshop.server.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public class RentalServiceImplTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private RentalMapper rentalMapper;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RentalServiceImpl rentalService;

    private AutoCloseable closeable;

    @BeforeEach
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testCreateRental_Success() {
        RentalRequestDTO requestDTO = RentalRequestDTO.builder()
                .rentalDate(null)
                .inventoryId(1)
                .customerId(2)
                .staffId(3)
                .rentalPeriod(7)
                .build();

        Rental rentalEntity = new Rental();
        Rental savedRental = new Rental();
        RentalResponseDTO responseDTO = RentalResponseDTO.builder()
                .id(100)
                .build();

        Inventory inventory = new Inventory();
        User customer = new User();
        User staff = new User();

        // Mock all repository dependencies
        when(inventoryRepository.findById(1)).thenReturn(Optional.of(inventory));
        when(userRepository.findById(2)).thenReturn(Optional.of(customer));
        when(userRepository.findById(3)).thenReturn(Optional.of(staff));

        when(rentalMapper.toEntity(requestDTO)).thenReturn(rentalEntity);
        when(rentalRepository.save(rentalEntity)).thenReturn(savedRental);
        when(rentalMapper.toResponseDto(savedRental)).thenReturn(responseDTO);

        RentalResponseDTO result = rentalService.createRental(requestDTO);

        assertEquals(responseDTO.getId(), result.getId());
        verify(rentalMapper).toEntity(requestDTO);
        verify(rentalRepository).save(rentalEntity);
        verify(rentalMapper).toResponseDto(savedRental);
    }

    @Test
    public void testCreateRental_InventoryNotFound() {
        RentalRequestDTO requestDTO = RentalRequestDTO.builder()
                .inventoryId(1)
                .customerId(2)
                .staffId(3)
                .build();

        when(inventoryRepository.findById(1)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> rentalService.createRental(requestDTO));

        assertEquals("Inventory not found with id: 1", ex.getMessage());
    }

    @Test
    public void testCreateRental_CustomerNotFound() {
        RentalRequestDTO requestDTO = RentalRequestDTO.builder()
                .inventoryId(1)
                .customerId(2)
                .staffId(3)
                .build();

        when(inventoryRepository.findById(1)).thenReturn(Optional.of(new Inventory()));
        when(userRepository.findById(2)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> rentalService.createRental(requestDTO));

        assertEquals("Customer not found with id: 2", ex.getMessage());
    }

    @Test
    public void testCreateRental_StaffNotFound() {
        RentalRequestDTO requestDTO = RentalRequestDTO.builder()
                .inventoryId(1)
                .customerId(2)
                .staffId(3)
                .build();

        when(inventoryRepository.findById(1)).thenReturn(Optional.of(new Inventory()));
        when(userRepository.findById(2)).thenReturn(Optional.of(new User()));
        when(userRepository.findById(3)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> rentalService.createRental(requestDTO));

        assertEquals("Staff not found with id: 3", ex.getMessage());
    }

    @Test
    public void testGetRentalById_Found() {
        Rental rental = new Rental();
        RentalResponseDTO responseDTO = new RentalResponseDTO();

        when(rentalRepository.findById(1)).thenReturn(Optional.of(rental));
        when(rentalMapper.toResponseDto(rental)).thenReturn(responseDTO);

        RentalResponseDTO result = rentalService.getRentalById(1);

        assertNotNull(result);
        verify(rentalRepository).findById(1);
        verify(rentalMapper).toResponseDto(rental);
    }

    @Test
    public void testGetRentalById_NotFound() {
        when(rentalRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> rentalService.getRentalById(999));
        verify(rentalRepository).findById(999);
    }

    @Test
    public void testGetAllRentals_ReturnsList() {
        List<Rental> rentals = List.of(new Rental(), new Rental());
        when(rentalRepository.findAll()).thenReturn(rentals);

        when(rentalMapper.toResponseDto(any(Rental.class)))
                .thenReturn(new RentalResponseDTO());

        List<RentalResponseDTO> result = rentalService.getAllRentals();

        assertEquals(rentals.size(), result.size());
        verify(rentalRepository).findAll();
        verify(rentalMapper, times(rentals.size())).toResponseDto(any(Rental.class));
    }

    @Test
    public void testUpdateRental_Success() {
        RentalRequestDTO requestDTO = RentalRequestDTO.builder()
                .rentalDate(OffsetDateTime.now())
                .inventoryId(1)
                .customerId(2)
                .staffId(3)
                .rentalPeriod(5)
                .build();

        Rental existingRental = new Rental();
        Inventory inventory = new Inventory();
        User customer = new User();
        User staff = new User();
        Rental updatedRental = new Rental();

        when(rentalRepository.findById(1)).thenReturn(Optional.of(existingRental));
        when(inventoryRepository.findById(1)).thenReturn(Optional.of(inventory));
        when(userRepository.findById(2)).thenReturn(Optional.of(customer));
        when(userRepository.findById(3)).thenReturn(Optional.of(staff));
        when(rentalRepository.save(any(Rental.class))).thenReturn(updatedRental);
        when(rentalMapper.toResponseDto(updatedRental)).thenReturn(new RentalResponseDTO());

        RentalResponseDTO result = rentalService.updateRental(1, requestDTO);

        assertNotNull(result);

        ArgumentCaptor<Rental> rentalCaptor = ArgumentCaptor.forClass(Rental.class);
        verify(rentalRepository).save(rentalCaptor.capture());

        Rental savedRental = rentalCaptor.getValue();
        assertEquals(inventory, savedRental.getInventory());
        assertEquals(customer, savedRental.getCustomer());
        assertEquals(staff, savedRental.getStaff());
        assertEquals(requestDTO.getRentalDate(), savedRental.getRentalDate());
        assertEquals(requestDTO.getRentalPeriod(), savedRental.getRentalPeriod());

        verify(rentalRepository).findById(1);
        verify(inventoryRepository).findById(1);
        verify(userRepository).findById(2);
        verify(userRepository).findById(3);
        verify(rentalMapper).toResponseDto(updatedRental);
    }

    @Test
    public void testUpdateRental_NotFound() {
        when(rentalRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> rentalService.updateRental(1, new RentalRequestDTO()));
        verify(rentalRepository).findById(1);
    }

    @Test
    public void testUpdateRental_InventoryNotFound() {
        Rental existingRental = new Rental();
        RentalRequestDTO requestDTO = RentalRequestDTO.builder()
                .inventoryId(999)
                .customerId(1)
                .staffId(1)
                .rentalPeriod(1)
                .rentalDate(OffsetDateTime.now())
                .build();

        when(rentalRepository.findById(1)).thenReturn(Optional.of(existingRental));
        when(inventoryRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> rentalService.updateRental(1, requestDTO));
        verify(rentalRepository).findById(1);
        verify(inventoryRepository).findById(999);
    }

    @Test
    public void testUpdateRental_CustomerNotFound() {
        Rental existingRental = new Rental();
        RentalRequestDTO requestDTO = RentalRequestDTO.builder()
                .inventoryId(1)
                .customerId(999)
                .staffId(1)
                .rentalPeriod(1)
                .rentalDate(OffsetDateTime.now())
                .build();

        when(rentalRepository.findById(1)).thenReturn(Optional.of(existingRental));
        when(inventoryRepository.findById(1)).thenReturn(Optional.of(new Inventory()));
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> rentalService.updateRental(1, requestDTO));
        verify(rentalRepository).findById(1);
        verify(inventoryRepository).findById(1);
        verify(userRepository).findById(999);
    }

    @Test
    public void testUpdateRental_StaffNotFound() {
        Rental existingRental = new Rental();
        RentalRequestDTO requestDTO = RentalRequestDTO.builder()
                .inventoryId(1)
                .customerId(1)
                .staffId(999)
                .rentalPeriod(1)
                .rentalDate(OffsetDateTime.now())
                .build();

        when(rentalRepository.findById(1)).thenReturn(Optional.of(existingRental));
        when(inventoryRepository.findById(1)).thenReturn(Optional.of(new Inventory()));
        when(userRepository.findById(1)).thenReturn(Optional.of(new User()));
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> rentalService.updateRental(1, requestDTO));
        verify(rentalRepository).findById(1);
        verify(inventoryRepository).findById(1);
        verify(userRepository).findById(1);
        verify(userRepository).findById(999);
    }

    @Test
    public void testUpdateRental_ShouldRemoveOldAssociations_WhenEntitiesChange() {
        Integer rentalId = 10;
        RentalRequestDTO dto = RentalRequestDTO.builder()
                .inventoryId(2)
                .customerId(3)
                .staffId(4)
                .build();

        Inventory oldInventory = mock(Inventory.class);
        User oldCustomer = mock(User.class);
        User oldStaff = mock(User.class);

        Rental existentRental = new Rental();
        existentRental.setInventory(oldInventory);
        existentRental.setCustomer(oldCustomer);
        existentRental.setStaff(oldStaff);

        when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(existentRental));
        when(inventoryRepository.findById(2)).thenReturn(Optional.of(new Inventory()));
        when(userRepository.findById(3)).thenReturn(Optional.of(new User()));
        when(userRepository.findById(4)).thenReturn(Optional.of(new User()));
        when(rentalRepository.save(any())).thenReturn(new Rental());
        when(rentalMapper.toResponseDto(any())).thenReturn(new RentalResponseDTO());

        rentalService.updateRental(rentalId, dto);

        verify(oldInventory).removeRental(existentRental);
        verify(oldCustomer).removeRental(existentRental);
        verify(oldStaff).removeRental(existentRental);
    }

    @Test
    public void testDeleteRental_Success() {
        Rental rental = new Rental();
        when(rentalRepository.findById(1)).thenReturn(Optional.of(rental));

        rentalService.deleteRental(1);

        verify(rentalRepository).findById(1);
        verify(rentalRepository).delete(rental);
    }

    @Test
    public void testDeleteRental_NotFound() {
        when(rentalRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> rentalService.deleteRental(1));
        verify(rentalRepository).findById(1);
    }
}
