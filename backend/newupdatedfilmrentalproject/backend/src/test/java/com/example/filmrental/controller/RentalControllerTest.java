
package com.example.filmrental.controller;

import com.example.filmrental.dto.RentalDto;
import com.example.filmrental.exception.ResourceNotFoundException;
import com.example.filmrental.service.RentalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Standalone unit tests for RentalController using Mockito + MockMvc.
 * Includes a TEST-ONLY @ControllerAdvice to map:
 *  - ResourceNotFoundException -> 404
 *  - Any other Exception        -> 500
 *
 * NOTE: RentalController endpoints covered:
 *  - POST   /api/rentals/add
 *  - GET    /api/rentals/customer/{id}
 *  - GET    /api/rentals/toptenfilms
 *  - GET    /api/rentals/toptenfilms/store/{id}
 *  - GET    /api/rentals/due/store/{id}
 *  - POST   /api/rentals/update/returndate/{id}
 */
class RentalControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private RentalService rentalService;

    @InjectMocks
    private RentalController rentalController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // ObjectMapper with JavaTimeModule to handle LocalDateTime serialization
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Wire test-only exception mapping so we can assert 404 for not-found cases
        mockMvc = MockMvcBuilders
                .standaloneSetup(rentalController)
                .setControllerAdvice(new TestExceptionHandler())
                .build();
    }

    // Test-only global exception handler to map exceptions to HTTP statuses
    @ControllerAdvice
    static class TestExceptionHandler {
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<String> handleGeneric(Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + ex.getMessage());
        }
    }

    private static final String BASE = "/api/rentals";

    // --------- POST: /add ---------
    @Test
    @DisplayName("POST /api/rentals/add => 200 OK with success message")
    void addRental_ok() throws Exception {
        RentalDto payload = new RentalDto(); // if no no-arg ctor, you can also mock and serialize

        // Service is void; no stubbing required for success
        mockMvc.perform(post(BASE + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().string("Record Created Successfully"));

        verify(rentalService).addRental(any(RentalDto.class));
    }

    @Test
    @DisplayName("POST /api/rentals/add => 500 when service throws RuntimeException")
    void addRental_genericError_returns500() throws Exception {
        RentalDto payload = new RentalDto();

        doThrow(new RuntimeException("DB down"))
                .when(rentalService).addRental(any(RentalDto.class));

        mockMvc.perform(post(BASE + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("DB down")));
    }

    // --------- GET: /customer/{id} ---------
    @Test
    @DisplayName("GET /api/rentals/customer/{id} => 200 with list of RentalDto")
    void getRentalsByCustomer_ok() throws Exception {
        Long customerId = 10L;
        when(rentalService.getRentalsByCustomerId(customerId))
                .thenReturn(Arrays.asList(new RentalDto(), new RentalDto()));

        mockMvc.perform(get(BASE + "/customer/{id}", customerId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));

        verify(rentalService).getRentalsByCustomerId(customerId);
    }

    @Test
    @DisplayName("GET /api/rentals/customer/{id} => 404 when not found")
    void getRentalsByCustomer_notFound_returns404() throws Exception {
        Long customerId = 404L;
        when(rentalService.getRentalsByCustomerId(customerId))
                .thenThrow(new ResourceNotFoundException("Customer not found"));

        mockMvc.perform(get(BASE + "/customer/{id}", customerId))
                .andExpect(status().isNotFound());
    }

    // --------- GET: /toptenfilms ---------
    @Test
    @DisplayName("GET /api/rentals/toptenfilms => 200 with list of RentalDto")
    void getTopTenFilms_ok() throws Exception {
        when(rentalService.getTopTenFilms())
                .thenReturn(Arrays.asList(new RentalDto(), new RentalDto(), new RentalDto()));

        mockMvc.perform(get(BASE + "/toptenfilms"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)));

        verify(rentalService).getTopTenFilms();
    }

    @Test
    @DisplayName("GET /api/rentals/toptenfilms => 500 when service throws")
    void getTopTenFilms_genericError_returns500() throws Exception {
        when(rentalService.getTopTenFilms()).thenThrow(new RuntimeException("Calc failed"));

        mockMvc.perform(get(BASE + "/toptenfilms"))
                .andExpect(status().isInternalServerError());
    }

    // --------- GET: /toptenfilms/store/{id} ---------
    @Test
    @DisplayName("GET /api/rentals/toptenfilms/store/{id} => 200 with list of RentalDto")
    void getTopTenFilmsByStore_ok() throws Exception {
        Long storeId = 2L;
        when(rentalService.getTopTenFilmsByStore(storeId))
                .thenReturn(Arrays.asList(new RentalDto(), new RentalDto()));

        mockMvc.perform(get(BASE + "/toptenfilms/store/{id}", storeId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));

        verify(rentalService).getTopTenFilmsByStore(storeId);
    }

    @Test
    @DisplayName("GET /api/rentals/toptenfilms/store/{id} => 500 when service throws")
    void getTopTenFilmsByStore_genericError_returns500() throws Exception {
        Long storeId = 100L;
        when(rentalService.getTopTenFilmsByStore(storeId))
                .thenThrow(new RuntimeException("No data"));

        mockMvc.perform(get(BASE + "/toptenfilms/store/{id}", storeId))
                .andExpect(status().isInternalServerError());
    }

    // --------- GET: /due/store/{id} ---------
    @Test
    @DisplayName("GET /api/rentals/due/store/{id} => 200 with Map<Long, String>")
    void getPendingReturnsByStore_ok() throws Exception {
        Long storeId = 3L;

        // Use LinkedHashMap to preserve order, if needed
        Map<Long, String> pending = new LinkedHashMap<>();
        pending.put(101L, "John Doe");
        pending.put(102L, "Jane Roe");

        when(rentalService.getCustomersWithPendingReturnsByStore(storeId)).thenReturn(pending);

        mockMvc.perform(get(BASE + "/due/store/{id}", storeId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                // Jackson serializes Long keys as strings, so use string keys in JSONPath
                .andExpect(jsonPath("$", hasKey("101")))
                .andExpect(jsonPath("$", hasKey("102")))
                .andExpect(jsonPath("$['101']").value("John Doe"))
                .andExpect(jsonPath("$['102']").value("Jane Roe"));

        verify(rentalService).getCustomersWithPendingReturnsByStore(storeId);
    }

    @Test
    @DisplayName("GET /api/rentals/due/store/{id} => 500 when service throws")
    void getPendingReturnsByStore_genericError_returns500() throws Exception {
        Long storeId = 3L;
        when(rentalService.getCustomersWithPendingReturnsByStore(storeId))
                .thenThrow(new RuntimeException("IO error"));

        mockMvc.perform(get(BASE + "/due/store/{id}", storeId))
                .andExpect(status().isInternalServerError());
    }

    // --------- POST: /update/returndate/{id} ---------
    @Test
    @DisplayName("POST /api/rentals/update/returndate/{id} => 200 with RentalDto")
    void updateReturnDate_ok() throws Exception {
        Long rentalId = 5L;
        LocalDateTime newReturnDate = LocalDateTime.now().plusDays(1);
        RentalDto updated = new RentalDto();

        when(rentalService.updateReturnDate(eq(rentalId), eq(newReturnDate)))
                .thenReturn(updated);

        mockMvc.perform(post(BASE + "/update/returndate/{id}", rentalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newReturnDate)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()));

        verify(rentalService).updateReturnDate(rentalId, newReturnDate);
    }

    @Test
    @DisplayName("POST /api/rentals/update/returndate/{id} => 404 when not found")
    void updateReturnDate_notFound_returns404() throws Exception {
        Long rentalId = 404L;
        LocalDateTime newReturnDate = LocalDateTime.now();

        when(rentalService.updateReturnDate(eq(rentalId), eq(newReturnDate)))
                .thenThrow(new ResourceNotFoundException("Rental not found"));

        mockMvc.perform(post(BASE + "/update/returndate/{id}", rentalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newReturnDate)))
                .andExpect(status().isNotFound());
    }
}
