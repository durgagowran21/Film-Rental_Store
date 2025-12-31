package com.example.filmrental.controller;
 
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.filmrental.dto.*;
import com.example.filmrental.exception.*;
import com.example.filmrental.model.*;
import com.example.filmrental.service.*;
 
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
 
    @Autowired
    private InventoryService inventoryService;
 
    @PostMapping("/add")
    public ResponseEntity<Inventory> createInventory(@RequestBody InventoryDTO inventoryDTO) throws ResourceNotFoundException {
        Inventory createdInventory = inventoryService.addFilmToStore(inventoryDTO);
        return new ResponseEntity<>(createdInventory, HttpStatus.CREATED);
    }

 
    @GetMapping("/films")
    public List<InventoryDTO> getAllFilmInventories() {
        return inventoryService.getAllFilmInventories();
    }
 
    @GetMapping("/store/{id}")
    public List<InventoryDTO> getFilmInventoriesByStore(@PathVariable Long id) {
        return inventoryService.getFilmInventoriesByStore(id);
    }
 
    @GetMapping("/film/{id}")
    public List<InventoryDTO> getStoreInventoriesByFilm(@PathVariable Long id) {
        return inventoryService.getStoreInventoriesByFilm(id);
    }
 
    @GetMapping("/film/{filmId}/store/{storeId}")
    public InventoryDTO getFilmInventoryByStore(@PathVariable Long filmId, @PathVariable Long storeId) {
        return inventoryService.getFilmInventoryByStore(filmId, storeId);
    }
}