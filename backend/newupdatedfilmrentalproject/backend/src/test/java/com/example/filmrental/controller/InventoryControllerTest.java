
package com.example.filmrental.controller;

import com.example.filmrental.dto.InventoryDTO;
import com.example.filmrental.model.Inventory;
import com.example.filmrental.service.InventoryService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pure unit tests for InventoryController (no Spring context).
 */
class InventoryControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private InventoryController inventoryController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();
    }

    private static final String BASE = "/api/inventory";

    // ---------- POST: /add ----------
    @Test
    @DisplayName("POST /api/inventory/add => 201 Created with Inventory body")
    void createInventory_created() throws Exception {
        InventoryDTO dto = new InventoryDTO();
        // dto.setFilmId(100L); dto.setStoreId(10L); dto.setQuantity(5);

        Inventory created = new Inventory();
        // created.setId(1L);

        when(inventoryService.addFilmToStore(any(InventoryDTO.class))).thenReturn(created);

        mockMvc.perform(post(BASE + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()));

        verify(inventoryService).addFilmToStore(any(InventoryDTO.class));
    }

    // ---------- GET: /films ----------
    @Test
    @DisplayName("GET /api/inventory/films => 200 with list of InventoryDTO")
    void getAllFilmInventories_ok() throws Exception {
        InventoryDTO d1 = new InventoryDTO();
        InventoryDTO d2 = new InventoryDTO();

        when(inventoryService.getAllFilmInventories()).thenReturn(List.of(d1, d2));

        mockMvc.perform(get(BASE + "/films"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));

        verify(inventoryService).getAllFilmInventories();
    }

    // ---------- GET: /store/{id} ----------
    @Test
    @DisplayName("GET /api/inventory/store/{id} => 200 with list of InventoryDTO")
    void getFilmInventoriesByStore_ok() throws Exception {
        long storeId = 10L;
        InventoryDTO d1 = new InventoryDTO();

        when(inventoryService.getFilmInventoriesByStore(storeId)).thenReturn(List.of(d1));

        mockMvc.perform(get(BASE + "/store/{id}", storeId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));

        verify(inventoryService).getFilmInventoriesByStore(storeId);
    }

    // ---------- GET: /film/{id} ----------
    @Test
    @DisplayName("GET /api/inventory/film/{id} => 200 with list of InventoryDTO")
    void getStoreInventoriesByFilm_ok() throws Exception {
        long filmId = 100L;
        InventoryDTO d1 = new InventoryDTO();
        InventoryDTO d2 = new InventoryDTO();

        when(inventoryService.getStoreInventoriesByFilm(filmId)).thenReturn(List.of(d1, d2));

        mockMvc.perform(get(BASE + "/film/{id}", filmId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));

        verify(inventoryService).getStoreInventoriesByFilm(filmId);
    }

    // ---------- GET: /film/{filmId}/store/{storeId} ----------
    @Test
    @DisplayName("GET /api/inventory/film/{filmId}/store/{storeId} => 200 with single InventoryDTO")
    void getFilmInventoryByStore_ok() throws Exception {
        long filmId = 100L;
        long storeId = 10L;
        InventoryDTO dto = new InventoryDTO();

        when(inventoryService.getFilmInventoryByStore(filmId, storeId)).thenReturn(dto);

        mockMvc.perform(get(BASE + "/film/{filmId}/store/{storeId}", filmId, storeId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()));

        verify(inventoryService).getFilmInventoryByStore(filmId, storeId);
    }
}
