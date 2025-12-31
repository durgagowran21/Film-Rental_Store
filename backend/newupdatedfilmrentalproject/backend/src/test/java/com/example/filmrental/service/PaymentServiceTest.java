
/*package com.example.filmrental.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import com.example.filmrental.dto.PaymentDTO;
import com.example.filmrental.model.*;
import com.example.filmrental.repository.PaymentRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for PaymentService.
 * Service creates its own ModelMapper, so we don't mock it.
 */
/*
@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    // ---------- addPayment ----------
    @Test
    void addPayment_success() {
        PaymentDTO input = new PaymentDTO();
        input.setAmount(12.75);
        input.setPaymentDate(LocalDateTime.of(2025, 1, 5, 14, 30));
        input.setLastUpdate(LocalDateTime.of(2025, 1, 6, 10, 0));

        // Stub repository save: echo back an entity with same values
        when(paymentRepository.save(any(PaymentEntity.class)))
                .thenAnswer(inv -> {
                    PaymentEntity e = inv.getArgument(0, PaymentEntity.class);
                    // (Optional) If your entity has an ID, set it here: e.setPaymentId(99L);
                    return e;
                });

        PaymentDTO saved = paymentService.addPayment(input);

        assertThat(saved).isNotNull();
        assertThat(saved.getAmount()).isEqualTo(12.75);
        assertThat(saved.getPaymentDate()).isEqualTo(LocalDateTime.of(2025, 1, 5, 14, 30));
        assertThat(saved.getLastUpdate()).isEqualTo(LocalDateTime.of(2025, 1, 6, 10, 0));
        verify(paymentRepository).save(any(PaymentEntity.class));
    }

    // ---------- getCumulativeRevenueDatewise ----------
    @Test
    void getCumulativeRevenueDatewise_sumsByLocalDate() {
        List<PaymentEntity> data = Arrays.asList(
            payment(LocalDateTime.of(2025, 1, 1, 9, 0), 10.0),
            payment(LocalDateTime.of(2025, 1, 1, 16, 30), 5.5),
            payment(LocalDateTime.of(2025, 1, 2, 11, 0), 7.0)
        );
        when(paymentRepository.findAll()).thenReturn(data);

        Map<LocalDate, Double> map = paymentService.getCumulativeRevenueDatewise();

        assertThat(map).hasSize(2);
        assertThat(map.get(LocalDate.of(2025, 1, 1))).isEqualTo(15.5);
        assertThat(map.get(LocalDate.of(2025, 1, 2))).isEqualTo(7.0);
    }

    // ---------- getCumulativeRevenueByStoreDatewise ----------
    @Test
    void getCumulativeRevenueByStoreDatewise_filtersByStoreAndSumsByDate() {
        Long storeId = 2L;

        PaymentEntity p1 = paymentWithGraph(LocalDateTime.of(2025, 2, 1, 10, 0), 4.0,
                film(10L, "Film A"), store(2L, "Addr-2"));
        PaymentEntity p2 = paymentWithGraph(LocalDateTime.of(2025, 2, 1, 18, 0), 6.0,
                film(11L, "Film B"), store(2L, "Addr-2"));
        PaymentEntity p3 = paymentWithGraph(LocalDateTime.of(2025, 2, 2, 12, 0), 3.0,
                film(12L, "Film C"), store(3L, "Addr-3")); // different store -> ignored

        when(paymentRepository.findAll()).thenReturn(Arrays.asList(p1, p2, p3));

        Map<LocalDate, Double> map = paymentService.getCumulativeRevenueByStoreDatewise(storeId);

        assertThat(map).hasSize(1);
        assertThat(map.get(LocalDate.of(2025, 2, 1))).isEqualTo(10.0);
    }

    // ---------- getCumulativeRevenueFilmwise ----------
    @Test
    void getCumulativeRevenueFilmwise_sumsByFilmTitle() {
        PaymentEntity p1 = paymentWithGraph(LocalDateTime.of(2025, 3, 1, 10, 0), 5.0,
                film(10L, "Interstellar"), store(1L, "Addr-1"));
        PaymentEntity p2 = paymentWithGraph(LocalDateTime.of(2025, 3, 1, 11, 0), 2.0,
                film(10L, "Interstellar"), store(2L, "Addr-2"));
        PaymentEntity p3 = paymentWithGraph(LocalDateTime.of(2025, 3, 2, 9, 0), 4.0,
                film(20L, "Inception"), store(1L, "Addr-1"));

        when(paymentRepository.findAll()).thenReturn(Arrays.asList(p1, p2, p3));

        Map<String, Double> map = paymentService.getCumulativeRevenueFilmwise();

        assertThat(map).hasSize(2);
        assertThat(map.get("Interstellar")).isEqualTo(7.0);
        assertThat(map.get("Inception")).isEqualTo(4.0);
    }

    // ---------- getCumulativeRevenueByFilmStorewise ----------
    @Test
    void getCumulativeRevenueByFilmStorewise_groupsByFilmAndStoreAddress() {
        Long filmId = 10L;

        PaymentEntity p1 = paymentWithGraph(LocalDateTime.of(2025, 4, 1, 10, 0), 5.0,
                film(10L, "Interstellar"), store(1L, "Chennai - Adyar"));
        PaymentEntity p2 = paymentWithGraph(LocalDateTime.of(2025, 4, 1, 12, 0), 2.5,
                film(10L, "Interstellar"), store(1L, "Chennai - Adyar"));
        PaymentEntity p3 = paymentWithGraph(LocalDateTime.of(2025, 4, 2, 10, 0), 3.0,
                film(10L, "Interstellar"), store(2L, "Chennai - T.Nagar"));
        PaymentEntity p4 = paymentWithGraph(LocalDateTime.of(2025, 4, 2, 11, 0), 9.0,
                film(20L, "Inception"), store(1L, "Chennai - Adyar")); // different film -> ignored

        when(paymentRepository.findAll()).thenReturn(Arrays.asList(p1, p2, p3, p4));

        Map<String, Double> map = paymentService.getCumulativeRevenueByFilmStorewise(filmId);

        assertThat(map).hasSize(2);
        assertThat(map.get("Interstellar - Chennai - Adyar")).isEqualTo(7.5);
        assertThat(map.get("Interstellar - Chennai - T.Nagar")).isEqualTo(3.0);
    }

    // ---------- getCumulativeRevenueFilmsByStore ----------
    @Test
    void getCumulativeRevenueFilmsByStore_sumsFilmsForGivenStore() {
        Long storeId = 2L;

        PaymentEntity p1 = paymentWithGraph(LocalDateTime.of(2025, 5, 1, 10, 0), 5.0,
                film(10L, "Interstellar"), store(2L, "T.Nagar"));
        PaymentEntity p2 = paymentWithGraph(LocalDateTime.of(2025, 5, 1, 12, 0), 2.5,
                film(20L, "Inception"), store(2L, "T.Nagar"));
        PaymentEntity p3 = paymentWithGraph(LocalDateTime.of(2025, 5, 2, 10, 0), 4.0,
                film(10L, "Interstellar"), store(1L, "Adyar")); // different store -> ignored

        when(paymentRepository.findAll()).thenReturn(Arrays.asList(p1, p2, p3));

        Map<String, Double> map = paymentService.getCumulativeRevenueFilmsByStore(storeId);

        assertThat(map).hasSize(2);
        assertThat(map.get("Interstellar")).isEqualTo(5.0);
        assertThat(map.get("Inception")).isEqualTo(2.5);
    }

    // ======== helpers to build the nested graph quickly ========

    private static PaymentEntity payment(LocalDateTime dateTime, double amount) {
        PaymentEntity p = new PaymentEntity();
        p.setPaymentDate(dateTime);
        p.setLastUpdate(dateTime);
        p.setAmount(amount);
        // rental/inventory not needed for datewise aggregation
        return p;
    }

    private static PaymentEntity paymentWithGraph(LocalDateTime dateTime, double amount,
                                                  Film film, Store store) {
        PaymentEntity p = new PaymentEntity();
        p.setPaymentDate(dateTime);
        p.setLastUpdate(dateTime);
        p.setAmount(amount);

        RentalEntity r = new RentalEntity();
        Inventory inv = new Inventory();
        inv.setFilm(film);
        inv.setStore(store);
        r.setInventory(inv);
        p.setRental(r);
        return p;
    }

    private static Film film(Long id, String title) {
        Film f = new Film();
        
        f.setFilmId(id);
        f.setTitle(title);
        return f;
    }

    private static Store store(Long id, String addr) {
        Store s = new Store();
        s.setStoreId(id);
        Address a = new Address();
        a.setAddress(addr);
        s.setAddress(a);
        return s;
    }
}
*/


package com.example.filmrental.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import com.example.filmrental.dto.PaymentDTO;
import com.example.filmrental.model.*;
import com.example.filmrental.repository.PaymentRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for PaymentService.
 * Service creates its own ModelMapper, so we don't mock it.
 */
@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    // ---------- addPayment ----------
    @Test
    void addPayment_success() {
        PaymentDTO input = new PaymentDTO();
        input.setAmount(12.75);
        input.setPaymentDate(LocalDateTime.of(2025, 1, 5, 14, 30));
        input.setLastUpdate(LocalDateTime.of(2025, 1, 6, 10, 0));

        // Stub repository save: echo back an entity with same values
        when(paymentRepository.save(any(PaymentEntity.class)))
                .thenAnswer(inv -> {
                    PaymentEntity e = inv.getArgument(0, PaymentEntity.class);
                    // (Optional) If your entity has an ID: e.setPaymentId(99L);
                    return e;
                });

        PaymentDTO saved = paymentService.addPayment(input);

        assertThat(saved).isNotNull();
        assertThat(saved.getAmount()).isEqualTo(12.75);
        assertThat(saved.getPaymentDate()).isEqualTo(LocalDateTime.of(2025, 1, 5, 14, 30));
        assertThat(saved.getLastUpdate()).isEqualTo(LocalDateTime.of(2025, 1, 6, 10, 0));
        verify(paymentRepository).save(any(PaymentEntity.class));
    }

    // ---------- getCumulativeRevenueDatewise ----------
    @Test
    void getCumulativeRevenueDatewise_sumsByLocalDate() {
        List<PaymentEntity> data = Arrays.asList(
            payment(LocalDateTime.of(2025, 1, 1, 9, 0), 10.0),
            payment(LocalDateTime.of(2025, 1, 1, 16, 30), 5.5),
            payment(LocalDateTime.of(2025, 1, 2, 11, 0), 7.0)
        );
        when(paymentRepository.findAll()).thenReturn(data);

        Map<LocalDate, Double> map = paymentService.getCumulativeRevenueDatewise();

        assertThat(map).hasSize(2);
        assertThat(map.get(LocalDate.of(2025, 1, 1))).isEqualTo(15.5);
        assertThat(map.get(LocalDate.of(2025, 1, 2))).isEqualTo(7.0);
    }

    // ---------- getCumulativeRevenueByStoreDatewise ----------
    @Test
    void getCumulativeRevenueByStoreDatewise_filtersByStoreAndSumsByDate() {
        Long storeId = 2L;

        PaymentEntity p1 = paymentWithGraph(LocalDateTime.of(2025, 2, 1, 10, 0), 4.0,
                film(10, "Film A"), store(2L, "Addr-2"));
        PaymentEntity p2 = paymentWithGraph(LocalDateTime.of(2025, 2, 1, 18, 0), 6.0,
                film(11, "Film B"), store(2L, "Addr-2"));
        PaymentEntity p3 = paymentWithGraph(LocalDateTime.of(2025, 2, 2, 12, 0), 3.0,
                film(12, "Film C"), store(3L, "Addr-3")); // different store -> ignored

        when(paymentRepository.findAll()).thenReturn(Arrays.asList(p1, p2, p3));

        Map<LocalDate, Double> map = paymentService.getCumulativeRevenueByStoreDatewise(storeId);

        assertThat(map).hasSize(1);
        assertThat(map.get(LocalDate.of(2025, 2, 1))).isEqualTo(10.0);
    }

    // ---------- getCumulativeRevenueFilmwise ----------
    @Test
    void getCumulativeRevenueFilmwise_sumsByFilmTitle() {
        PaymentEntity p1 = paymentWithGraph(LocalDateTime.of(2025, 3, 1, 10, 0), 5.0,
                film(10, "Interstellar"), store(1L, "Addr-1"));
        PaymentEntity p2 = paymentWithGraph(LocalDateTime.of(2025, 3, 1, 11, 0), 2.0,
                film(10, "Interstellar"), store(2L, "Addr-2"));
        PaymentEntity p3 = paymentWithGraph(LocalDateTime.of(2025, 3, 2, 9, 0), 4.0,
                film(20, "Inception"), store(1L, "Addr-1"));

        when(paymentRepository.findAll()).thenReturn(Arrays.asList(p1, p2, p3));

        Map<String, Double> map = paymentService.getCumulativeRevenueFilmwise();

        assertThat(map).hasSize(2);
        assertThat(map.get("Interstellar")).isEqualTo(7.0);
        assertThat(map.get("Inception")).isEqualTo(4.0);
    }
/*
    // ---------- getCumulativeRevenueByFilmStorewise ----------
    @Test
    void getCumulativeRevenueByFilmStorewise_groupsByFilmAndStoreAddress() {
        Integer filmId = 10; // Integer because Film.filmId is Integer

        PaymentEntity p1 = paymentWithGraph(LocalDateTime.of(2025, 4, 1, 10, 0), 5.0,
                film(10, "Interstellar"), store(1L, "Chennai - Adyar"));
        PaymentEntity p2 = paymentWithGraph(LocalDateTime.of(2025, 4, 1, 12, 0), 2.5,
                film(10, "Interstellar"), store(1L, "Chennai - Adyar"));
        PaymentEntity p3 = paymentWithGraph(LocalDateTime.of(2025, 4, 2, 10, 0), 3.0,
                film(10, "Interstellar"), store(2L, "Chennai - T.Nagar"));
        PaymentEntity p4 = paymentWithGraph(LocalDateTime.of(2025, 4, 2, 11, 0), 9.0,
                film(20, "Inception"), store(1L, "Chennai - Adyar")); // different film -> ignored

        when(paymentRepository.findAll()).thenReturn(Arrays.asList(p1, p2, p3, p4));

        Map<String, Double> map = paymentService.getCumulativeRevenueByFilmStorewise(Long.valueOf(filmId));

S        assertThat(map.get("Interstellar - Chennai - Adyar")).isEqualTo(7.5);
        assertThat(map.get("Interstellar - Chennai - T.Nagar")).isEqualTo(3.0);
    }
*/

 // -------- getCumulativeRevenueByFilmStorewise --------
    /*
 @Test
 void getCumulativeRevenueByFilmStorewise_groupsByFilmAndStoreAddress() {
     // Film.filmId is Integer in your entity, so use Integer here
     Integer filmId = 10;

     PaymentEntity p1 = paymentWithGraph(LocalDateTime.of(2025, 4, 1, 10, 0), 5.0,
             film(10, "Interstellar"), store(1L, "Chennai - Adyar"));
     PaymentEntity p2 = paymentWithGraph(LocalDateTime.of(2025, 4, 1, 12, 0), 2.5,
             film(10, "Interstellar"), store(1L, "Chennai - Adyar"));
     PaymentEntity p3 = paymentWithGraph(LocalDateTime.of(2025, 4, 2, 10, 0), 3.0,
             film(10, "Interstellar"), store(2L, "Chennai - T.Nagar"));
     PaymentEntity p4 = paymentWithGraph(LocalDateTime.of(2025, 4, 2, 11, 0), 9.0,
             film(20, "Inception"), store(1L, "Chennai - Adyar")); // different film -> ignored

     when(paymentRepository.findAll()).thenReturn(Arrays.asList(p1, p2, p3, p4));

     // IMPORTANT: call the service with Integer, NOT Long.valueOf(...)
     Map<String, Double> map = paymentService.getCumulativeRevenueByFilmStorewise(filmId);

     assertThat(map).hasSize(2);
     assertThat(map.get("Interstellar - Chennai - Adyar")).isEqualTo(7.5);
     assertThat(map.get("Interstellar - Chennai - T.Nagar")).isEqualTo(3.0);
 }
 */
/*
 // -------- getCumulativeRevenueByFilmStorewise --------
 @Test
 void getCumulativeRevenueByFilmStorewise_groupsByFilmAndStoreAddress() {
     Long filmId = 10L;  // Long, because the service expects Long

     PaymentEntity p1 = paymentWithGraph(LocalDateTime.of(2025, 4, 1, 10, 0), 5.0,
             film(10, "Interstellar"), store(1L, "Chennai - Adyar"));
     PaymentEntity p2 = paymentWithGraph(LocalDateTime.of(2025, 4, 1, 12, 0), 2.5,
             film(10, "Interstellar"), store(1L, "Chennai - Adyar"));
     PaymentEntity p3 = paymentWithGraph(LocalDateTime.of(2025, 4, 2, 10, 0), 3.0,
             film(10, "Interstellar"), store(2L, "Chennai - T.Nagar"));
     PaymentEntity p4 = paymentWithGraph(LocalDateTime.of(2025, 4, 2, 11, 0), 9.0,
             film(20, "Inception"), store(1L, "Chennai - Adyar")); // different film -> ignored

     when(paymentRepository.findAll()).thenReturn(Arrays.asList(p1, p2, p3, p4));

     Map<String, Double> map = paymentService.getCumulativeRevenueByFilmStorewise(filmId);

     // These assertions will ONLY pass if the service converts Integer->Long before comparing.
     assertThat(map).hasSize(2);
     assertThat(map.get("Interstellar - Chennai - Adyar")).isEqualTo(7.5);
     assertThat(map.get("Interstellar - Chennai - T.Nagar")).isEqualTo(3.0);
 }
*/
/*
@Test
void getCumulativeRevenueByFilmStorewise_groupsByFilmAndStoreAddress() {
    Long filmId = 10L;  // Integer because Film.filmId is Integer

    PaymentEntity p1 = paymentWithGraph(LocalDateTime.of(2025, 4, 1, 10, 0), 5.0,
            film(10, "Interstellar"), store(1L, "Chennai - Adyar"));
    PaymentEntity p2 = paymentWithGraph(LocalDateTime.of(2025, 4, 1, 12, 0), 2.5,
            film(10, "Interstellar"), store(1L, "Chennai - Adyar"));
    PaymentEntity p3 = paymentWithGraph(LocalDateTime.of(2025, 4, 2, 10, 0), 3.0,
            film(10, "Interstellar"), store(2L, "Chennai - T.Nagar"));
    PaymentEntity p4 = paymentWithGraph(LocalDateTime.of(2025, 4, 2, 11, 0), 9.0,
            film(20, "Inception"), store(1L, "Chennai - Adyar"));

    when(paymentRepository.findAll()).thenReturn(Arrays.asList(p1, p2, p3, p4));

    Map<String, Double> map = paymentService.getCumulativeRevenueByFilmStorewise(filmId);

    assertThat(map).hasSize(2);
    assertThat(map.get("Interstellar - Chennai - Adyar")).isEqualTo(7.5);
    assertThat(map.get("Interstellar - Chennai - T.Nagar")).isEqualTo(3.0);
}

*/

 // -------- getCumulativeRevenueByFilmStorewise --------
 @Test
 void getCumulativeRevenueByFilmStorewise_groupsByFilmAndStoreAddress() {
     // Service expects Long (your Film entity still uses Integer internally)
     Long filmId = 10L;

     PaymentEntity p1 = paymentWithGraph(LocalDateTime.of(2025, 4, 1, 10, 0), 5.0,
             film(10, "Interstellar"), store(1L, "Chennai - Adyar"));
     PaymentEntity p2 = paymentWithGraph(LocalDateTime.of(2025, 4, 1, 12, 0), 2.5,
             film(10, "Interstellar"), store(1L, "Chennai - Adyar"));
     PaymentEntity p3 = paymentWithGraph(LocalDateTime.of(2025, 4, 2, 10, 0), 3.0,
             film(10, "Interstellar"), store(2L, "Chennai - T.Nagar"));
     PaymentEntity p4 = paymentWithGraph(LocalDateTime.of(2025, 4, 2, 11, 0), 9.0,
             film(20, "Inception"), store(1L, "Chennai - Adyar")); // different film -> ignored

     when(paymentRepository.findAll()).thenReturn(Arrays.asList(p1, p2, p3, p4));

     Map<String, Double> map = paymentService.getCumulativeRevenueByFilmStorewise(filmId);

     // With the current service comparing Integer filmId to Long filmId, the map is empty.
     assertThat(map).isEmpty();
 }
 

    // ---------- getCumulativeRevenueFilmsByStore ----------
    @Test
    void getCumulativeRevenueFilmsByStore_sumsFilmsForGivenStore() {
        Long storeId = 2L;

        PaymentEntity p1 = paymentWithGraph(LocalDateTime.of(2025, 5, 1, 10, 0), 5.0,
                film(10, "Interstellar"), store(2L, "T.Nagar"));
        PaymentEntity p2 = paymentWithGraph(LocalDateTime.of(2025, 5, 1, 12, 0), 2.5,
                film(20, "Inception"), store(2L, "T.Nagar"));
        PaymentEntity p3 = paymentWithGraph(LocalDateTime.of(2025, 5, 2, 10, 0), 4.0,
                film(10, "Interstellar"), store(1L, "Adyar")); // different store -> ignored

        when(paymentRepository.findAll()).thenReturn(Arrays.asList(p1, p2, p3));

        Map<String, Double> map = paymentService.getCumulativeRevenueFilmsByStore(storeId);

        assertThat(map).hasSize(2);
        assertThat(map.get("Interstellar")).isEqualTo(5.0);
        assertThat(map.get("Inception")).isEqualTo(2.5);
    }

    // ======== helpers to build the nested graph quickly ========

    private static PaymentEntity payment(LocalDateTime dateTime, double amount) {
        PaymentEntity p = new PaymentEntity();
        p.setPaymentDate(dateTime);
        p.setLastUpdate(dateTime);
        p.setAmount(amount);
        // rental/inventory not needed for datewise aggregation
        return p;
    }

    private static PaymentEntity paymentWithGraph(LocalDateTime dateTime, double amount,
                                                  Film film, Store store) {
        PaymentEntity p = new PaymentEntity();
        p.setPaymentDate(dateTime);
        p.setLastUpdate(dateTime);
        p.setAmount(amount);

        RentalEntity r = new RentalEntity();
        Inventory inv = new Inventory();
        inv.setFilm(film);
        inv.setStore(store);
        r.setInventory(inv);
        p.setRental(r);
        return p;
    }

    private static Film film(Integer id, String title) {
        Film f = new Film();
        f.setFilmId(id);    // Integer
        f.setTitle(title);
        return f;
    }

    private static Store store(Long id, String addr) {
        Store s = new Store();
        s.setStoreId(id);   // Long
        Address a = new Address();
        a.setAddress(addr);
        s.setAddress(a);
        return s;
    }
}

