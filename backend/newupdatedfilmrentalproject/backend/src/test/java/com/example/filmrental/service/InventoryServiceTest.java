
package com.example.filmrental.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.example.filmrental.dto.InventoryDTO;
import com.example.filmrental.exception.ResourceNotFoundException;
import com.example.filmrental.model.Film;
import com.example.filmrental.model.Inventory;
import com.example.filmrental.model.Store;
import com.example.filmrental.repository.FilmRepository;
import com.example.filmrental.repository.InventoryRepository;
import com.example.filmrental.repository.StoreRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock private InventoryRepository inventoryRepository;
    @Mock private FilmRepository filmRepository;
    @Mock private StoreRepository storeRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    void getAllFilmInventories_success() {
        Film f1 = new Film(); f1.setFilmId(1); // If Long in your entity, use 1L
        Store s1 = new Store(); s1.setStoreId(100L);
        Inventory i1 = new Inventory(); i1.setInventoryId(10L); i1.setFilm(f1); i1.setStore(s1); i1.setLastUpdate(LocalDateTime.now());

        Film f2 = new Film(); f2.setFilmId(2);
        Store s2 = new Store(); s2.setStoreId(200L);
        Inventory i2 = new Inventory(); i2.setInventoryId(20L); i2.setFilm(f2); i2.setStore(s2); i2.setLastUpdate(LocalDateTime.now());

        when(inventoryRepository.findAll()).thenReturn(Arrays.asList(i1, i2));

        List<InventoryDTO> result = inventoryService.getAllFilmInventories();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getInventoryId()).isEqualTo(10L);
        assertThat(result.get(0).getFilmId()).isEqualTo(1);
        assertThat(result.get(0).getStoreId()).isEqualTo(100L);
    }

    @Test
    void getStoreInventoriesByFilm_success() {
        Film f = new Film(); f.setFilmId(5);
        Store s = new Store(); s.setStoreId(1L);
        Inventory i = new Inventory(); i.setInventoryId(11L); i.setFilm(f); i.setStore(s); i.setLastUpdate(LocalDateTime.now());

        when(inventoryRepository.findByFilm_FilmId(5L)).thenReturn(Arrays.asList(i));

        List<InventoryDTO> result = inventoryService.getStoreInventoriesByFilm(5L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFilmId()).isEqualTo(5);
        assertThat(result.get(0).getStoreId()).isEqualTo(1L);
    }

    @Test
    void getFilmInventoriesByStore_success() {
        Film f = new Film(); f.setFilmId(6);
        Store s = new Store(); s.setStoreId(3L);
        Inventory i = new Inventory(); i.setInventoryId(12L); i.setFilm(f); i.setStore(s); i.setLastUpdate(LocalDateTime.now());

        when(inventoryRepository.findByStore_StoreId(3L)).thenReturn(Arrays.asList(i));

        List<InventoryDTO> result = inventoryService.getFilmInventoriesByStore(3L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getInventoryId()).isEqualTo(12L);
        assertThat(result.get(0).getStoreId()).isEqualTo(3L);
        assertThat(result.get(0).getFilmId()).isEqualTo(6);
    }

    @Test
    void getFilmInventoryByStore_returnsFirstOrNull() {
        Film f = new Film(); f.setFilmId(7);
        Store s = new Store(); s.setStoreId(4L);
        Inventory i = new Inventory(); i.setInventoryId(13L); i.setFilm(f); i.setStore(s); i.setLastUpdate(LocalDateTime.now());
       // setLastUpdate();

        when(inventoryRepository.findByFilm_FilmIdAndStore_StoreId(7L, 4L)).thenReturn(Arrays.asList(i));

        InventoryDTO result = inventoryService.getFilmInventoryByStore(7L, 4L);

        assertThat(result).isNotNull();
        assertThat(result.getInventoryId()).isEqualTo(13L);

        when(inventoryRepository.findByFilm_FilmIdAndStore_StoreId(8L, 9L)).thenReturn(Collections.emptyList());
        assertThat(inventoryService.getFilmInventoryByStore(8L, 9L)).isNull();
    }

//    @Test
//    void addFilmToStore_success() throws Exception {
//        InventoryDTO dto = new InventoryDTO();
//        dto.setFilmId(15);      // If Film id is Long in your entity, change DTO to Long and set 15L
//        dto.setStoreId(2L);
//        dto.setLastUpdate(LocalDateTime.now());
//
//        Film film = new Film(); film.setFilmId(15);
//        Store store = new Store(); store.setStoreId(2L);
//
//        when(filmRepository.findById(15)).thenReturn(Optional.of(film));
//        when(storeRepository.findById(2L)).thenReturn(Optional.of(store));
//
//        Inventory toSave = new Inventory();
//        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(inv -> {
//            Inventory invent = invent.getArgument(0);
//            invent.setInventoryId(99L);
//            return inv;
//        });
//
//        Inventory saved = inventoryService.addFilmToStore(dto);
//
//        assertThat(saved.getInventoryId()).isEqualTo(99L);
//        assertThat(saved.getFilm()).isEqualTo(film);
//        assertThat(saved.getStore()).isEqualTo(store);
//        verify(inventoryRepository).save(any(Inventory.class));
//    }

    
    
    @Test
    void addFilmToStore_nullIds_throwsIllegalArgumentException() {
        InventoryDTO dto = new InventoryDTO();
        dto.setFilmId(null);
        dto.setStoreId(null);

        assertThrows(IllegalArgumentException.class, () -> inventoryService.addFilmToStore(dto));
    }

    @Test
    void addFilmToStore_filmNotFound_throwsResourceNotFound() {
        InventoryDTO dto = new InventoryDTO();
        dto.setFilmId(123);
        dto.setStoreId(2L);

        when(filmRepository.findById(123)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inventoryService.addFilmToStore(dto));
    }

    @Test
    void addFilmToStore_storeNotFound_throwsResourceNotFound() {
        InventoryDTO dto = new InventoryDTO();
        dto.setFilmId(123);
        dto.setStoreId(2L);

        Film film = new Film(); film.setFilmId(123);
        when(filmRepository.findById(123)).thenReturn(Optional.of(film));
        when(storeRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inventoryService.addFilmToStore(dto));
    }

    @Test
    void convertToDto_handlesNullFilmOrStore() { 
        Inventory inv = new Inventory();
        inv.setInventoryId(50L);
        inv.setLastUpdate(null);
        // inv.setLastUpdate(Instant.now());
        inv.setFilm(null);
        inv.setStore(null);

        when(inventoryRepository.findAll()).thenReturn(Arrays.asList(inv));

        List<InventoryDTO> result = inventoryService.getAllFilmInventories();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getInventoryId()).isEqualTo(50L);
        assertThat(result.get(0).getFilmId()).isNull();
        assertThat(result.get(0).getStoreId()).isNull();
    }
}
