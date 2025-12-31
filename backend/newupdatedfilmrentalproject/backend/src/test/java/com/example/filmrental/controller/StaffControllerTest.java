
package com.example.filmrental.controller;

import com.example.filmrental.dto.StaffDTO;
import com.example.filmrental.dto.StaffAllUpdateRequestDto.StaffAddressAssignRequest;
import com.example.filmrental.dto.StaffAllUpdateRequestDto.StaffEmailUpdateRequest;
import com.example.filmrental.dto.StaffAllUpdateRequestDto.StaffFirstNameUpdateRequest;
import com.example.filmrental.dto.StaffAllUpdateRequestDto.StaffLastNameUpdateRequest;
import com.example.filmrental.dto.StaffAllUpdateRequestDto.StaffPhoneUpdateRequest;
import com.example.filmrental.dto.StaffAllUpdateRequestDto.StaffStoreAssignRequest;
import com.example.filmrental.exception.BadRequestException;
import com.example.filmrental.exception.ResourceNotFoundException;
import com.example.filmrental.service.StaffService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

class StaffControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StaffService staffServ;

    @InjectMocks
    private StaffController controller;

    private AutoCloseable closeable;
    private ObjectMapper objectMapper;

    // Minimal exception handler so negative tests get proper status codes
    @ControllerAdvice
    static class TestExceptionHandler {
        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<?> badRequest(BadRequestException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<?> notFound(ResourceNotFoundException ex) {
            return ResponseEntity.status(404).body(Map.of("error", ex.getMessage()));
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

    // ------------------------------
    // PUT /Staff/api/staff/update/address/{id}
    // ------------------------------
    @Test
    @DisplayName("PUT /Staff/api/staff/update/address/{id} => 200 OK with StaffDTO")
    void assignAddress_ok() throws Exception {
        long staffId = 1L;
        long addressId = 10L;
        StaffDTO updated = mock(StaffDTO.class);

        when(staffServ.assignAddress(eq(staffId), eq(addressId))).thenReturn(updated);

        StaffAddressAssignRequest body = new StaffAddressAssignRequest(addressId);

        mockMvc.perform(put("/Staff/api/staff/update/address/{id}", staffId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));

        verify(staffServ).assignAddress(eq(staffId), eq(addressId));
    }

//    @Test
//    @DisplayName("PUT /Staff/api/staff/update/address/{id} => 400 when addressId is null")
//    void assignAddress_badRequest_nullAddressId() throws Exception {
//        long staffId = 1L;
//        // body without addressId (or addressId = null)
//        String body = "{}";
//
//        // Simulate service validating and throwing BadRequestException
//        when(staffServ.assignAddress(eq(staffId), isNull()))
//                .thenThrow(new BadRequestException("addressId must not be null"));
//
//        mockMvc.perform(put("/Staff/api/staff/update/address/{id}", staffId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.error").value("addressId must not be null"));
//
//        verify(staffServ).assignAddress(eq(staffId), isNull());
//    }

    @Test
    @DisplayName("PUT /Staff/api/staff/update/address/{id} => 404 when staff not found")
    void assignAddress_notFound_staff() throws Exception {
        long staffId = 999L;
        long addressId = 10L;
        StaffAddressAssignRequest body = new StaffAddressAssignRequest(addressId);

        when(staffServ.assignAddress(eq(staffId), eq(addressId)))
                .thenThrow(new ResourceNotFoundException("Staff not found: " + staffId));

        mockMvc.perform(put("/Staff/api/staff/update/address/{id}", staffId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Staff not found: " + staffId));

        verify(staffServ).assignAddress(eq(staffId), eq(addressId));
    }

    @Test
    @DisplayName("PUT /Staff/api/staff/update/address/{id} => 404 when address not found")
    void assignAddress_notFound_address() throws Exception {
        long staffId = 1L;
        long addressId = 999L;
        StaffAddressAssignRequest body = new StaffAddressAssignRequest(addressId);

        when(staffServ.assignAddress(eq(staffId), eq(addressId)))
                .thenThrow(new ResourceNotFoundException("Address not found: " + addressId));

        mockMvc.perform(put("/Staff/api/staff/update/address/{id}", staffId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Address not found: " + addressId));

        verify(staffServ).assignAddress(eq(staffId), eq(addressId));
    }

    // ------------------------------
    // PUT /Staff/api/staff/update/store/{id}
    // ------------------------------
    @Test
    @DisplayName("PUT /Staff/api/staff/update/store/{id} => 200 OK with StaffDTO")
    void assignStore_ok() throws Exception {
        long staffId = 1L;
        long storeId = 20L;
        StaffDTO updated = mock(StaffDTO.class);

        when(staffServ.updateStore(eq(staffId), eq(storeId))).thenReturn(updated);

        StaffStoreAssignRequest body = new StaffStoreAssignRequest(storeId);

        mockMvc.perform(put("/Staff/api/staff/update/store/{id}", staffId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));

        verify(staffServ).updateStore(eq(staffId), eq(storeId));
    }

//    @Test
//    @DisplayName("PUT /Staff/api/staff/update/store/{id} => 400 when storeId is null")
//    void assignStore_badRequest_nullStoreId() throws Exception {
//        long staffId = 1L;
//        String body = "{}";
//
//        when(staffServ.updateStore(eq(staffId), isNull()))
//                .thenThrow(new BadRequestException("storeId must not be null"));
//
//        mockMvc.perform(put("/Staff/api/staff/update/store/{id}", staffId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(body))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.error").value("storeId must not be null"));
//
//        verify(staffServ).updateStore(eq(staffId), isNull());
//    }

    @Test
    @DisplayName("PUT /Staff/api/staff/update/store/{id} => 404 when staff not found")
    void assignStore_notFound_staff() throws Exception {
        long staffId = 999L;
        long storeId = 20L;
        StaffStoreAssignRequest body = new StaffStoreAssignRequest(storeId);

        when(staffServ.updateStore(eq(staffId), eq(storeId)))
                .thenThrow(new ResourceNotFoundException("Staff not found: " + staffId));

        mockMvc.perform(put("/Staff/api/staff/update/store/{id}", staffId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Staff not found: " + staffId));

        verify(staffServ).updateStore(eq(staffId), eq(storeId));
    }

    @Test
    @DisplayName("PUT /Staff/api/staff/update/store/{id} => 404 when store not found")
    void assignStore_notFound_store() throws Exception {
        long staffId = 1L;
        long storeId = 999L;
        StaffStoreAssignRequest body = new StaffStoreAssignRequest(storeId);

        when(staffServ.updateStore(eq(staffId), eq(storeId)))
                .thenThrow(new ResourceNotFoundException("Store not found: " + storeId));

        mockMvc.perform(put("/Staff/api/staff/update/store/{id}", staffId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Store not found: " + storeId));

        verify(staffServ).updateStore(eq(staffId), eq(storeId));
    }

    // ------------------------------
    // Simple update endpoints
    // ------------------------------
    @Test
    @DisplayName("PUT /Staff/{id}/first-name => 200 OK")
    void updateFirstName_ok() throws Exception {
        long id = 1L;
        StaffDTO updated = mock(StaffDTO.class);
        when(staffServ.updateFirstName(eq(id), eq("Durga"))).thenReturn(updated);

        StaffFirstNameUpdateRequest body = new StaffFirstNameUpdateRequest("Durga");

        mockMvc.perform(put("/Staff/{id}/first-name", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));

        verify(staffServ).updateFirstName(eq(id), eq("Durga"));
    }

    @Test
    @DisplayName("PUT /Staff/{id}/last-name => 200 OK")
    void updateLastName_ok() throws Exception {
        long id = 1L;
        StaffDTO updated = mock(StaffDTO.class);
        when(staffServ.updateLastName(eq(id), eq("Gowran"))).thenReturn(updated);

        StaffLastNameUpdateRequest body = new StaffLastNameUpdateRequest("Gowran");

        mockMvc.perform(put("/Staff/{id}/last-name", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));

        verify(staffServ).updateLastName(eq(id), eq("Gowran"));
    }

    @Test
    @DisplayName("PUT /Staff/{id}/email => 200 OK")
    void updateEmail_ok() throws Exception {
        long id = 1L;
        StaffDTO updated = mock(StaffDTO.class);
        when(staffServ.updateEmail(eq(id), eq("durga@example.com"))).thenReturn(updated);

        StaffEmailUpdateRequest body = new StaffEmailUpdateRequest("durga@example.com");

        mockMvc.perform(put("/Staff/{id}/email", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));

        verify(staffServ).updateEmail(eq(id), eq("durga@example.com"));
    }

    @Test
    @DisplayName("PUT /Staff/update/phone/{id} => 200 OK")
    void updatePhone_ok() throws Exception {
        long id = 1L;
        StaffDTO updated = mock(StaffDTO.class);
        when(staffServ.updatePhoneNumberInAddress(eq(id), eq("9876543210")))
                .thenReturn(updated);

        StaffPhoneUpdateRequest body = new StaffPhoneUpdateRequest("9876543210");

        mockMvc.perform(put("/Staff/update/phone/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));

        verify(staffServ).updatePhoneNumberInAddress(eq(id), eq("9876543210"));
    }
}
