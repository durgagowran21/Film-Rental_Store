
package com.example.filmrental.controller;

import com.example.filmrental.dto.CustomerDto;
import com.example.filmrental.exception.ResourceNotFoundException;
import com.example.filmrental.model.Store;
import com.example.filmrental.service.CustomerService;
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

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pure unit tests for CustomerController using Mockito + standalone MockMvc.
 * No Spring context is started here.
 */
class CustomerControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    private static final String BASE = "/api/customers";

    // ---------- POST: /post ----------
    @Test
    @DisplayName("POST /api/customers/post => 201 Created with success message")
    void createCustomer_created() throws Exception {
        CustomerDto payload = new CustomerDto();
        // Set fields on payload if needed: payload.setFirstName("John"); etc.

        mockMvc.perform(post(BASE + "/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Record Created Successfully"));

        verify(customerService).createCustomer(any(CustomerDto.class));
    }

    // ---------- GET: /lastname/{lastName} ----------
    @Test
    @DisplayName("GET /api/customers/lastname/{lastName} => 200 with list")
    void getCustomersByLastName_ok() throws Exception {
        when(customerService.getCustomersByLastName("Doe"))
                .thenReturn(List.of(new CustomerDto(), new CustomerDto()));

        mockMvc.perform(get(BASE + "/lastname/{lastName}", "Doe"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));

        verify(customerService).getCustomersByLastName("Doe");
    }

    // ---------- GET: /firstname/{firstName} ----------
    @Test
    @DisplayName("GET /api/customers/firstname/{firstName} => 200 with list")
    void getCustomersByFirstName_ok() throws Exception {
        when(customerService.getCustomersByFirstName("John"))
                .thenReturn(List.of(new CustomerDto()));

        mockMvc.perform(get(BASE + "/firstname/{firstName}", "John"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));

        verify(customerService).getCustomersByFirstName("John");
    }

    // ---------- GET: /email/{email} ----------
    @Test
    @DisplayName("GET /api/customers/email/{email} => 200 with list")
    void getCustomersByEmail_ok() throws Exception {
        when(customerService.getCustomersByEmail("john@example.com"))
                .thenReturn(List.of(new CustomerDto()));

        mockMvc.perform(get(BASE + "/email/{email}", "john@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));

        verify(customerService).getCustomersByEmail("john@example.com");
    }

    // ---------- GET: /active ----------
    @Test
    @DisplayName("GET /api/customers/active => 200 with list")
    void getActiveCustomers_ok() throws Exception {
        when(customerService.getActiveCustomers())
                .thenReturn(List.of(new CustomerDto(), new CustomerDto(), new CustomerDto()));

        mockMvc.perform(get(BASE + "/active"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)));

        verify(customerService).getActiveCustomers();
    }

    // ---------- GET: /inactive ----------
    @Test
    @DisplayName("GET /api/customers/inactive => 200 with list")
    void getInactiveCustomers_ok() throws Exception {
        when(customerService.getInactiveCustomers())
                .thenReturn(List.of(new CustomerDto()));

        mockMvc.perform(get(BASE + "/inactive"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));

        verify(customerService).getInactiveCustomers();
    }

    // ---------- GET: /phone/{phone} ----------
    @Test
    @DisplayName("GET /api/customers/phone/{phone} => 200 with list")
    void getCustomersByPhone_ok() throws Exception {
        when(customerService.getCustomersByPhone("9999999999"))
                .thenReturn(List.of(new CustomerDto(), new CustomerDto()));

        mockMvc.perform(get(BASE + "/phone/{phone}", "9999999999"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));

        verify(customerService).getCustomersByPhone("9999999999");
    }

    // ---------- GET: /city/{city} ----------
    @Test
    @DisplayName("GET /api/customers/city/{city} => 200 with list")
    void getCustomersByCity_ok() throws Exception {
        when(customerService.getCustomersByCityName("Chennai"))
                .thenReturn(List.of(new CustomerDto()));

        mockMvc.perform(get(BASE + "/city/{city}", "Chennai"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));

        verify(customerService).getCustomersByCityName("Chennai");
    }

    // ---------- GET: /country/{country} ----------
    @Test
    @DisplayName("GET /api/customers/country/{country} => 200 with list")
    void getCustomersByCountry_ok() throws Exception {
        when(customerService.getCustomersByCountryName("India"))
                .thenReturn(List.of(new CustomerDto(), new CustomerDto()));

        mockMvc.perform(get(BASE + "/country/{country}", "India"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));

        verify(customerService).getCustomersByCountryName("India");
    }

    // ---------- PUT: /update/{id}/firstname (String body) ----------
    @Test
    @DisplayName("PUT /api/customers/update/{id}/firstname => 200 with updated CustomerDto")
    void updateFirstName_ok() throws Exception {
        long id = 1L;
        String newFirstName = "Mivetha";
        CustomerDto updated = new CustomerDto();
        // updated.setFirstName(newFirstName);

        when(customerService.updateFirstName(eq(id), eq(newFirstName))).thenReturn(updated);

        mockMvc.perform(put(BASE + "/update/{id}/firstname", id)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(newFirstName))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()));

        verify(customerService).updateFirstName(id, newFirstName);
    }

    // ---------- PUT: /update/{id}/lastname (String body) ----------
    @Test
    @DisplayName("PUT /api/customers/update/{id}/lastname => 200 with updated CustomerDto")
    void updateLastName_ok() throws Exception {
        long id = 1L;
        String newLastName = "K";
        CustomerDto updated = new CustomerDto();
        // updated.setLastName(newLastName);

        when(customerService.updateLastName(eq(id), eq(newLastName))).thenReturn(updated);

        mockMvc.perform(put(BASE + "/update/{id}/lastname", id)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(newLastName))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()));

        verify(customerService).updateLastName(id, newLastName);
    }

    // ---------- PUT: /updateemail/{id} (String body) ----------
    @Test
    @DisplayName("PUT /api/customers/updateemail/{id} => 200 with updated CustomerDto")
    void updateEmail_ok() throws Exception {
        long id = 1L;
        String newEmail = "mivetha.k@example.com";
        CustomerDto updated = new CustomerDto();
        // updated.setEmail(newEmail);

        when(customerService.updateEmail(eq(id), eq(newEmail))).thenReturn(updated);

        mockMvc.perform(put(BASE + "/updateemail/{id}", id)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(newEmail))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()));

        verify(customerService).updateEmail(id, newEmail);
    }

    // ---------- PUT: /updatephone/{id} (String body) ----------
    @Test
    @DisplayName("PUT /api/customers/updatephone/{id} => 200 with updated CustomerDto")
    void updatePhone_ok() throws Exception {
        long id = 1L;
        String newPhone = "9876543210";
        CustomerDto updated = new CustomerDto();
        // updated.setPhone(newPhone);

        when(customerService.updateCustomerPhone(eq(id), eq(newPhone))).thenReturn(updated);

        mockMvc.perform(put(BASE + "/updatephone/{id}", id)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(newPhone))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()));

        verify(customerService).updateCustomerPhone(id, newPhone);
    }

    // ---------- PUT: /{id}/{addressId} (path variables) ----------
    @Test
    @DisplayName("PUT /api/customers/{id}/{addressId} => 200 with updated CustomerDto")
    void updateCustomerAddress_ok() throws Exception {
        long id = 1L;
        long addressId = 99L;
        CustomerDto updated = new CustomerDto();

        when(customerService.assignAddressToCustomer(eq(id), eq(addressId))).thenReturn(updated);

        mockMvc.perform(put(BASE + "/{id}/{addressId}", id, addressId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()));

        verify(customerService).assignAddressToCustomer(id, addressId);
    }

    // ---------- PUT: /update/{id}/store (Store body) ----------
    @Test
    @DisplayName("PUT /api/customers/update/{id}/store => 200 with updated CustomerDto")
    void updateCustomerStore_ok() throws Exception {
        long id = 1L;
        Store store = new Store();
        // store.setStoreId(10L);

        CustomerDto updated = new CustomerDto();

        when(customerService.assignStoreToCustomer(eq(id), any(Store.class))).thenReturn(updated);

        mockMvc.perform(put(BASE + "/update/{id}/store", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(store)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()));

        verify(customerService).assignStoreToCustomer(eq(id), any(Store.class));
    }

    
   
}
