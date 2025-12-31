
package com.example.filmrental.controller;

import com.example.filmrental.dto.PaymentDTO;
import com.example.filmrental.exception.InvalidInputException;
import com.example.filmrental.exception.ResourceNotFoundException;
import com.example.filmrental.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pure unit test for PaymentController using Mockito + standalone MockMvc.
 * No Spring context is started here.
 */
class PaymentControllerTest {

    private MockMvc mockMvc;

    // You can reuse a single ObjectMapper; configure if you need custom modules.
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        // Build MockMvc around the controller. This picks up controller-local @ExceptionHandler methods.
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }

    private static final String BASE = "/api/payment";

    // ---------- PUT: /add ----------
    @Test
    @DisplayName("PUT /api/payment/add => 200 OK when amount > 0")
    void addPayment_ok() throws Exception {
        PaymentDTO dto = new PaymentDTO();
        dto.setAmount(100.00);

        mockMvc.perform(put(BASE + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Record Created Successfully"));

        verify(paymentService).addPayment(any(PaymentDTO.class));
    }

    @Test
    @DisplayName("PUT /api/payment/add => 400 Bad Request when amount <= 0 (handled by controller)")
    void addPayment_invalidAmount_returnsBadRequest() throws Exception {
        PaymentDTO dto = new PaymentDTO();
        dto.setAmount(0.0); // triggers InvalidInputException in controller

        mockMvc.perform(put(BASE + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid input: Payment amount must be greater than zero.")));
    }

    @Test
    @DisplayName("PUT /api/payment/add => 500 Internal Server Error when service throws generic Exception (handled by controller)")
    void addPayment_genericError_returns500() throws Exception {
        PaymentDTO dto = new PaymentDTO();
        dto.setAmount(10.0);

        doThrow(new RuntimeException("DB down"))
                .when(paymentService).addPayment(any(PaymentDTO.class));

        mockMvc.perform(put(BASE + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("An unexpected error occurred: DB down")));
    }

    // ---------- GET: /revenue/datewise ----------
    @Test
    @DisplayName("GET /api/payment/revenue/datewise => 200 with Map<LocalDate, Double>")
    void getRevenueDatewise_ok() throws Exception {
        LocalDate d1 = LocalDate.now().minusDays(2);
        LocalDate d2 = LocalDate.now().minusDays(1);
        Map<LocalDate, Double> revenue = new LinkedHashMap<>();
        revenue.put(d1, 150.50);
        revenue.put(d2, 75.25);

        when(paymentService.getCumulativeRevenueDatewise()).thenReturn(revenue);

        mockMvc.perform(get(BASE + "/revenue/datewise"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                // LocalDate keys serialize as ISO strings, e.g., "2025-12-22"
                .andExpect(jsonPath("$", hasKey(d1.toString())))
                .andExpect(jsonPath("$", hasKey(d2.toString())))
                .andExpect(jsonPath("$['" + d1 + "']").value(is(150.50)))
                .andExpect(jsonPath("$['" + d2 + "']").value(is(75.25)));

        verify(paymentService).getCumulativeRevenueDatewise();
    }

    // ---------- GET: /revenue/datewise/store/{id} ----------
    @Test
    @DisplayName("GET /api/payment/revenue/datewise/store/{id} => 200 with Map<LocalDate, Double>")
    void getRevenueByStoreDatewise_ok() throws Exception {
        long storeId = 10L;
        LocalDate d1 = LocalDate.now().minusDays(3);
        Map<LocalDate, Double> revenue = new LinkedHashMap<>();
        revenue.put(d1, 999.99);

        when(paymentService.getCumulativeRevenueByStoreDatewise(storeId)).thenReturn(revenue);

        mockMvc.perform(get(BASE + "/revenue/datewise/store/{id}", storeId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasKey(d1.toString())))
                .andExpect(jsonPath("$['" + d1 + "']").value(is(999.99)));

        verify(paymentService).getCumulativeRevenueByStoreDatewise(storeId);
    }

    @Test
    @DisplayName("GET /api/payment/revenue/datewise/store/{id} => 404 when empty (controller throws ResourceNotFoundException)")
    void getRevenueByStoreDatewise_empty_returnsNotFound() throws Exception {
        long storeId = 999L;

        when(paymentService.getCumulativeRevenueByStoreDatewise(storeId)).thenReturn(Map.of());

        mockMvc.perform(get(BASE + "/revenue/datewise/store/{id}", storeId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Error: No revenue found for the store ID: " + storeId)));
    }

    // ---------- GET: /revenue/filmwise ----------
    @Test
    @DisplayName("GET /api/payment/revenue/filmwise => 200 with Map<String, Double>")
    void getRevenueFilmwise_ok() throws Exception {
        Map<String, Double> revenue = new LinkedHashMap<>();
        revenue.put("Inception", 1200.00);
        revenue.put("Interstellar", 800.50);

        when(paymentService.getCumulativeRevenueFilmwise()).thenReturn(revenue);

        mockMvc.perform(get(BASE + "/revenue/filmwise"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Inception").value(is(1200.00)))
                .andExpect(jsonPath("$.Interstellar").value(is(800.50)));

        verify(paymentService).getCumulativeRevenueFilmwise();
    }

    // ---------- GET: /revenue/film/{id} ----------
    @Test
    @DisplayName("GET /api/payment/revenue/film/{id} => 200 with Map<String, Double>")
    void getRevenueByFilmStorewise_ok() throws Exception {
        long filmId = 100L;
        Map<String, Double> revenue = new LinkedHashMap<>();
        revenue.put("Store-1", 300.00);
        revenue.put("Store-2", 450.75);

        when(paymentService.getCumulativeRevenueByFilmStorewise(filmId)).thenReturn(revenue);

        mockMvc.perform(get(BASE + "/revenue/film/{id}", filmId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.['Store-1']").value(is(300.00)))
                .andExpect(jsonPath("$.['Store-2']").value(is(450.75)));

        verify(paymentService).getCumulativeRevenueByFilmStorewise(filmId);
    }

    @Test
    @DisplayName("GET /api/payment/revenue/film/{id} => 404 when empty (controller throws ResourceNotFoundException)")
    void getRevenueByFilmStorewise_empty_returnsNotFound() throws Exception {
        long filmId = 404L;

        when(paymentService.getCumulativeRevenueByFilmStorewise(filmId)).thenReturn(Map.of());

        mockMvc.perform(get(BASE + "/revenue/film/{id}", filmId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Error: No revenue found for the film ID: " + filmId)));
    }

    // ---------- GET: /revenue/films/store/{id} ----------
    @Test
    @DisplayName("GET /api/payment/revenue/films/store/{id} => 200 with Map<String, Double>")
    void getRevenueFilmsByStore_ok() throws Exception {
        long storeId = 10L;
        Map<String, Double> revenue = new LinkedHashMap<>();
        revenue.put("Inception", 700.00);
        revenue.put("Memento", 150.25);

        when(paymentService.getCumulativeRevenueFilmsByStore(storeId)).thenReturn(revenue);

        mockMvc.perform(get(BASE + "/revenue/films/store/{id}", storeId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Inception").value(is(700.00)))
                .andExpect(jsonPath("$.Memento").value(is(150.25)));

        verify(paymentService).getCumulativeRevenueFilmsByStore(storeId);
    }

    @Test
    @DisplayName("GET /api/payment/revenue/films/store/{id} => 404 when empty (controller throws ResourceNotFoundException)")
    void getRevenueFilmsByStore_empty_returnsNotFound() throws Exception {
        long storeId = 999L;

        when(paymentService.getCumulativeRevenueFilmsByStore(storeId)).thenReturn(Map.of());

        mockMvc.perform(get(BASE + "/revenue/films/store/{id}", storeId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Error: No revenue found for the store ID: " + storeId)));
    }
}
