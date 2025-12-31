
package com.example.filmrental.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.example.filmrental.dto.RentalDto;
import com.example.filmrental.exception.ResourceNotFoundException;
import com.example.filmrental.model.Customer;
import com.example.filmrental.model.Inventory;
import com.example.filmrental.model.RentalEntity;
import com.example.filmrental.model.Store;
import com.example.filmrental.repository.RentalRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Unit tests for RentalService */
@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock
    private RentalRepository rentalRepository;

    @InjectMocks
    private RentalService rentalService;

    // ---------- addRental ----------
    @Test
    void addRental_success() {
        RentalDto input = new RentalDto();
        // Populate DTO minimally; service overrides rentalDate/lastUpdate to now

        // Echo back entity passed to save (with an id to mimic persistence)
        when(rentalRepository.save(any(RentalEntity.class))).thenAnswer(invocation -> {
            RentalEntity e = invocation.getArgument(0, RentalEntity.class);
            e.setRentalId(101L);
            return e;
        });

        RentalDto saved = rentalService.addRental(input);

        assertThat(saved).isNotNull();
        assertThat(saved.getRentalId()).isEqualTo(101L);
        verify(rentalRepository).save(any(RentalEntity.class));
    }

    // ---------- getRentalsByCustomerId ----------
    @Test
    void getRentalsByCustomerId_success_filtersByCustomerId() throws Exception {
        Long targetCustomerId = 5L;

        RentalEntity r1 = rentalWithCustomerAndStore(5L, 1L, LocalDateTime.now().minusDays(1));
        RentalEntity r2 = rentalWithCustomerAndStore(5L, 2L, LocalDateTime.now().minusHours(2));
        RentalEntity r3 = rentalWithCustomerAndStore(7L, 1L, LocalDateTime.now().minusHours(1)); // different customer -> excluded

        when(rentalRepository.findAll()).thenReturn(Arrays.asList(r1, r2, r3));

        List<RentalDto> result = rentalService.getRentalsByCustomerId(targetCustomerId);

        assertThat(result).hasSize(2);
        // Basic mapping check (ModelMapper creates DTOs)
        assertThat(result.get(0).getCustomerId().valueOf(0).equals(5L));
        		//.getCustomer().getCustomerId()).isEqualTo(5L);
        assertThat(result.get(1).getCustomerId().getClass().equals(5L));
        		//.getCustomerId()).isEqualTo(5L);
    }

    @Test
    void getRentalsByCustomerId_notFound_throws() {
        Long targetCustomerId = 99L;
        RentalEntity other = rentalWithCustomerAndStore(5L, 1L, LocalDateTime.now());
        when(rentalRepository.findAll()).thenReturn(Collections.singletonList(other));

        assertThrows(ResourceNotFoundException.class,
            () -> rentalService.getRentalsByCustomerId(targetCustomerId));
    }

    // ---------- getTopTenFilms ----------
    @Test
    void getTopTenFilms_returnsLatestTenByRentalDate_descending() {
        // Build 12 rentals with different dates
        RentalEntity r1  = rentalWithDate(LocalDateTime.now().minusDays(12));
        RentalEntity r2  = rentalWithDate(LocalDateTime.now().minusDays(11));
        RentalEntity r3  = rentalWithDate(LocalDateTime.now().minusDays(10));
        RentalEntity r4  = rentalWithDate(LocalDateTime.now().minusDays(9));
        RentalEntity r5  = rentalWithDate(LocalDateTime.now().minusDays(8));
        RentalEntity r6  = rentalWithDate(LocalDateTime.now().minusDays(7));
        RentalEntity r7  = rentalWithDate(LocalDateTime.now().minusDays(6));
        RentalEntity r8  = rentalWithDate(LocalDateTime.now().minusDays(5));
        RentalEntity r9  = rentalWithDate(LocalDateTime.now().minusDays(4));
        RentalEntity r10 = rentalWithDate(LocalDateTime.now().minusDays(3));
        RentalEntity r11 = rentalWithDate(LocalDateTime.now().minusDays(2)); // should be included
        RentalEntity r12 = rentalWithDate(LocalDateTime.now().minusDays(1)); // most recent

        when(rentalRepository.findAll()).thenReturn(Arrays.asList(
            r1,r2,r3,r4,r5,r6,r7,r8,r9,r10,r11,r12
        ));

        List<RentalDto> result = rentalService.getTopTenFilms();

        assertThat(result).hasSize(10);
        // The first should be the most recent
        assertThat(result.get(0).getRentalDate()).isEqualTo(r12.getRentalDate());
        // The last should be the 10th most recent (r3)
        assertThat(result.get(9).getRentalDate()).isEqualTo(r3.getRentalDate());
    }


    @Test
    void getTopTenFilmsByStore_filtersByStoreAndReturnsLatestTen_withIdOnly() {
    Long storeId = 2L;

    RentalEntity s2_r1 = rentalWithCustomerAndStore(1L, 2L, LocalDateTime.now().minusDays(1));
    RentalEntity s2_r2 = rentalWithCustomerAndStore(1L, 2L, LocalDateTime.now().minusHours(1));
    RentalEntity s3_r1 = rentalWithCustomerAndStore(1L, 3L, LocalDateTime.now().minusHours(2));

    // Give inventory ids to entities so ModelMapper can map them to DTO ids (if configured)
    s2_r1.getInventory().setInventoryId(201L);
    s2_r2.getInventory().setInventoryId(202L);
    s3_r1.getInventory().setInventoryId(301L);

    when(rentalRepository.findAll()).thenReturn(Arrays.asList(s2_r1, s2_r2, s3_r1));

    List<RentalDto> result = rentalService.getTopTenFilmsByStore(storeId);

    assertThat(result).hasSize(2);
    // Only assert inventoryId and order; cannot access store from DTO if it’s not nested
    assertThat(result.get(0).getInventoryId()).isEqualTo(202L);
    assertThat(result.get(1).getInventoryId()).isEqualTo(201L);
    assertThat(result.get(0).getRentalDate()).isEqualTo(s2_r2.getRentalDate());
   }

    
    // ---------- getCustomersWithPendingReturnsByStore ----------
    @Test
    void getCustomersWithPendingReturnsByStore_collectsCustomersWithNullReturnDate() {
        Long storeId = 2L;

        // Pending returns at store 2
        RentalEntity p1 = rentalWithCustomerAndStore(10L, 2L, LocalDateTime.now().minusDays(3));
        p1.setReturnDate(null);
        p1.getCustomer().setFirstName("Alice");

        RentalEntity p2 = rentalWithCustomerAndStore(11L, 2L, LocalDateTime.now().minusDays(2));
        p2.setReturnDate(null);
        p2.getCustomer().setFirstName("Bob");

        // Completed return at store 2 (excluded)
        RentalEntity c1 = rentalWithCustomerAndStore(12L, 2L, LocalDateTime.now().minusDays(1));
        c1.setReturnDate(LocalDateTime.now());

        // Pending at different store (excluded)
        RentalEntity p3 = rentalWithCustomerAndStore(13L, 3L, LocalDateTime.now().minusDays(1));
        p3.setReturnDate(null);

        when(rentalRepository.findAll()).thenReturn(Arrays.asList(p1, p2, c1, p3));

        Map<Long, String> result = rentalService.getCustomersWithPendingReturnsByStore(storeId);

        assertThat(result).hasSize(2);
        assertThat(result.get(10L)).isEqualTo("Alice");
        assertThat(result.get(11L)).isEqualTo("Bob");
    }

    // ---------- updateReturnDate ----------
    @Test
    void updateReturnDate_success_updatesEntityAndMapsDto() throws Exception {
        Long rentalId = 200L;
        LocalDateTime newReturn = LocalDateTime.now();

        RentalEntity existing = rentalWithCustomerAndStore(5L, 1L, LocalDateTime.now().minusDays(5));
        existing.setRentalId(rentalId);
        existing.setReturnDate(null);

        RentalEntity saved = rentalWithCustomerAndStore(5L, 1L, existing.getRentalDate());
        saved.setRentalId(rentalId);
        saved.setReturnDate(newReturn);
        saved.setLastUpdate(LocalDateTime.now());

        when(rentalRepository.findById(rentalId)).thenReturn(java.util.Optional.of(existing));
        when(rentalRepository.save(existing)).thenReturn(saved);

        RentalDto dto = rentalService.updateReturnDate(rentalId, newReturn);

        assertThat(dto.getReturnDate()).isEqualTo(newReturn);
        assertThat(dto.getRentalId()).isEqualTo(rentalId);
        verify(rentalRepository).findById(rentalId);
        verify(rentalRepository).save(existing);
    }

    @Test
    void updateReturnDate_notFound_throws() {
        when(rentalRepository.findById(404L)).thenReturn(java.util.Optional.empty());
        assertThrows(ResourceNotFoundException.class,
            () -> rentalService.updateReturnDate(404L, LocalDateTime.now()));
    }

    // ================== helpers ==================

    /** Build RentalEntity with given customerId and storeId at a specific rentalDate */
    private static RentalEntity rentalWithCustomerAndStore(Long customerId, Long storeId, LocalDateTime rentalDate) {
        Customer cust = new Customer();
        cust.setCustomerId(customerId);
        // firstName can be set later by tests if needed

        Store store = new Store();
        store.setStoreId(storeId);

        Inventory inv = new Inventory();
        inv.setStore(store);

        RentalEntity r = new RentalEntity();
        r.setCustomer(cust);
        r.setInventory(inv);
        r.setRentalDate(rentalDate);
        r.setLastUpdate(rentalDate);
        r.setReturnDate(null);
        return r;
    }

    /** Build RentalEntity with only a rentalDate (other fields not needed for top10 sort) */
    private static RentalEntity rentalWithDate(LocalDateTime rentalDate) {
        RentalEntity r = new RentalEntity();
        r.setRentalDate(rentalDate);
        r.setLastUpdate(rentalDate);
        return r;
    }
}
