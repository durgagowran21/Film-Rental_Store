
package com.example.filmrental.controller;

import com.example.filmrental.dto.CustomerDto;
import com.example.filmrental.dto.StoreDto;
import com.example.filmrental.dto.StoreIdDTO;
import com.example.filmrental.exception.BadRequestException;
import com.example.filmrental.exception.InvalidInputException;
import com.example.filmrental.exception.ResourceNotFoundException;
import com.example.filmrental.model.Staff;
import com.example.filmrental.model.Store;
import com.example.filmrental.service.StoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class StoreControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StoreService storeService;

    @InjectMocks
    private StoreController controller;

    private AutoCloseable closeable;
    private ObjectMapper objectMapper;

    // Minimal exception handler for standalone tests
    @ControllerAdvice
    static class TestExceptionHandler {
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<?> notFound(ResourceNotFoundException ex) {
            return ResponseEntity.status(404).body(Map.of("error", ex.getMessage()));
        }
        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<?> badRequest(BadRequestException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
        @ExceptionHandler(InvalidInputException.class)
        public ResponseEntity<?> invalidInput(InvalidInputException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new TestExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    // -----------------------------
    // GET /api/stores/all
    // -----------------------------
    @Test
    @DisplayName("GET /api/stores/all => 200 with list of Store")
    void getAllStores_ok() throws Exception {
        when(storeService.getAllStores()).thenReturn(List.of(new Store(), new Store()));

        mockMvc.perform(get("/api/stores/all"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)));

        verify(storeService).getAllStores();
    }

    // -----------------------------
    // POST /api/stores/add
    // -----------------------------
//    @Test
//    @DisplayName("POST /api/stores/add => 201 Created with Store")
//    void addStore_created() throws Exception {
//        StoreDto req = new StoreDto(); // fill fields if needed
//        Store created = new Store();
//        when(storeService.addStore(any(StoreDto.class))).thenReturn(created);
//
//        mockMvc.perform(post("/api/stores/add")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(req)))
//               .andExpect(status().isCreated())
//               .andExpect(jsonPath("$").exists());
//
//        verify(storeService).addStore(any(StoreDto.class));
//    }

    // -----------------------------
    // PUT /api/stores/{storeId}/address/{addressId}
    // -----------------------------
    @Test
    @DisplayName("PUT /api/stores/{storeId}/address/{addressId} => 200 OK with StoreDto")
    void assignAddressToStore_ok() throws Exception {
        long storeId = 1L, addressId = 99L;
        StoreDto updated = new StoreDto();
        when(storeService.assignAddressToStore(eq(storeId), eq(addressId))).thenReturn(updated);

        mockMvc.perform(put("/api/stores/{storeId}/address/{addressId}", storeId, addressId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").exists());

        verify(storeService).assignAddressToStore(eq(storeId), eq(addressId));
    }

    @Test
    @DisplayName("PUT /api/stores/{storeId}/address/{addressId} => 404 when store not found")
    void assignAddressToStore_notFound_store() throws Exception {
        long storeId = 999L, addressId = 99L;
        when(storeService.assignAddressToStore(eq(storeId), eq(addressId)))
                .thenThrow(new ResourceNotFoundException("Store not found: " + storeId));

        mockMvc.perform(put("/api/stores/{storeId}/address/{addressId}", storeId, addressId))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.error").value("Store not found: " + storeId));

        verify(storeService).assignAddressToStore(eq(storeId), eq(addressId));
    }

    @Test
    @DisplayName("PUT /api/stores/{storeId}/address/{addressId} => 404 when address not found")
    void assignAddressToStore_notFound_address() throws Exception {
        long storeId = 1L, addressId = 999L;
        when(storeService.assignAddressToStore(eq(storeId), eq(addressId)))
                .thenThrow(new ResourceNotFoundException("Address not found: " + addressId));

        mockMvc.perform(put("/api/stores/{storeId}/address/{addressId}", storeId, addressId))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.error").value("Address not found: " + addressId));

        verify(storeService).assignAddressToStore(eq(storeId), eq(addressId));
    }

    // -----------------------------
    // GET /api/stores/city/{city}
    // -----------------------------
    @Test
    @DisplayName("GET /api/stores/city/{city} => 200 with List<StoreDto>")
    void getStoresByCity_ok() throws Exception {
        String city = "Chennai";
        when(storeService.getStoresByCity(eq(city))).thenReturn(List.of(new StoreDto(), new StoreDto()));

        mockMvc.perform(get("/api/stores/city/{city}", city))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)));

        verify(storeService).getStoresByCity(eq(city));
    }

    // -----------------------------
    // GET /api/stores/country/{country}
    // -----------------------------
//    @Test
//    @DisplayName("GET /api/stores/country/{country} => 200 with List<StoreIdDTO>")
//    void getStoresByCountry_ok() throws Exception {
//        String country = "India";
//        when(storeService.getStoreIdsByCountry(eq(country))).thenReturn(List.of(new StoreIdDTO(1L), new StoreIdDTO(2L)));
//
//        mockMvc.perform(get("/api/stores/country/{country}", country))
//               .andExpect(status().isOk())
//               .andExpect(jsonPath("$", hasSize(2)));
//
//        verify(storeService).getStoreIdsByCountry(eq(country));
//    }

    // -----------------------------
    // GET /api/stores/phone/{phone}
    // -----------------------------
    @Test
    @DisplayName("GET /api/stores/phone/{phone} => 200 with StoreDto")
    void getStoreByPhone_ok() throws Exception {
        String phone = "044-1234567";
        when(storeService.getStoreByPhone(eq(phone))).thenReturn(new StoreDto());

        mockMvc.perform(get("/api/stores/phone/{phone}", phone))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").exists());

        verify(storeService).getStoreByPhone(eq(phone));
    }

    @Test
    @DisplayName("GET /api/stores/phone/{phone} => 404 when not found")
    void getStoreByPhone_notFound() throws Exception {
        String phone = "000-0000000";
        when(storeService.getStoreByPhone(eq(phone)))
                .thenThrow(new ResourceNotFoundException("Store not found for phone: " + phone));

        mockMvc.perform(get("/api/stores/phone/{phone}", phone))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.error").value("Store not found for phone: " + phone));

        verify(storeService).getStoreByPhone(eq(phone));
    }

    // -----------------------------
    // PUT /api/stores/update/{storeId}/{phone}
    // -----------------------------
    @Test
    @DisplayName("PUT /api/stores/update/{storeId}/{phone} => 200 with message")
    void updateStorePhoneNumber_ok() throws Exception {
        long storeId = 1L;
        String phone = "044-7654321";
        when(storeService.updatePhoneNumber(eq(storeId), eq(phone)))
                .thenReturn("Phone updated");

        mockMvc.perform(put("/api/stores/update/{storeId}/{phone}", storeId, phone))
               .andExpect(status().isOk())
               .andExpect(content().string("Phone updated"));

        verify(storeService).updatePhoneNumber(eq(storeId), eq(phone));
    }

//    @Test
//    @DisplayName("PUT /api/stores/update/{storeId}/{phone} => 400 when invalid input")
//    void updateStorePhoneNumber_badRequest_invalidPhone() throws Exception {
//        long storeId = 1L;
//        String phone = "invalid";
//        when(storeService.updatePhoneNumber(eq(storeId), eq(phone)))
//                .thenThrow(new InvalidInputException("Invalid phone format"));
//
//        mockMvc.perform(put("/api/stores/update/{storeId}/{phone}", storeId, phone))
//               .andExpect(status().isBadRequest())
//               .andExpect(jsonPath("$.error").value("Invalid phone format"));
//
//        verify(storeService).updatePhoneNumber(eq(storeId), eq(phone));
//    }

    @Test
    @DisplayName("PUT /api/stores/update/{storeId}/{phone} => 400 when store not found (controller catches and returns 400)")
    void updateStorePhoneNumber_notFound_mappedTo400() throws Exception {
        long storeId = 999L;
        String phone = "044-7654321";

        // Your controller catches ResourceNotFoundException and returns bad request (400)
        when(storeService.updatePhoneNumber(eq(storeId), eq(phone)))
                .thenThrow(new ResourceNotFoundException("Store not found: " + storeId));

        mockMvc.perform(put("/api/stores/update/{storeId}/{phone}", storeId, phone))
               .andExpect(status().isBadRequest())
               .andExpect(content().string("Store not found: " + storeId));

        verify(storeService).updatePhoneNumber(eq(storeId), eq(phone));
    }

    // -----------------------------
    // GET /api/stores/staff/{storeId}
    // -----------------------------
    @Test
    @DisplayName("GET /api/stores/staff/{storeId} => 200 with List<Staff>")
    void getStaffByStoreId_ok() throws Exception {
        long storeId = 1L;
        when(storeService.getStaffByStoreId(eq(storeId)))
                .thenReturn(List.of(new Staff(), new Staff(), new Staff()));

        mockMvc.perform(get("/api/stores/staff/{storeId}", storeId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(3)));

        verify(storeService).getStaffByStoreId(eq(storeId));
    }

    // -----------------------------
    // GET /api/stores/customer/{storeId}
    // -----------------------------
    @Test
    @DisplayName("GET /api/stores/customer/{storeId} => 200 with List<CustomerDto>")
    void getCustomersByStore_ok() throws Exception {
        long storeId = 1L;
        when(storeService.getCustomersByStoreId(eq(storeId)))
                .thenReturn(List.of(new CustomerDto(), new CustomerDto()));

        mockMvc.perform(get("/api/stores/customer/{storeId}", storeId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)));

        verify(storeService).getCustomersByStoreId(eq(storeId));
    }

    @Test
    @DisplayName("GET /api/stores/customer/{storeId} => 404 when store not found")
    void getCustomersByStore_notFound() throws Exception {
        long storeId = 999L;
        when(storeService.getCustomersByStoreId(eq(storeId)))
                .thenThrow(new ResourceNotFoundException("Store not found: " + storeId));

        mockMvc.perform(get("/api/stores/customer/{storeId}", storeId))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.error").value("Store not found: " + storeId));

        verify(storeService).getCustomersByStoreId(eq(storeId));
    }
}
