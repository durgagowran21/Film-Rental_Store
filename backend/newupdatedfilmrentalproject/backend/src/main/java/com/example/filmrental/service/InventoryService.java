//package com.example.filmrental.service;
// 
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.example.filmrental.dto.*;
//import com.example.filmrental.exception.*;
//import com.example.filmrental.repository.*;
//import com.example.filmrental.model.*;
// 
//@Service
//public class InventoryService {
// 
//    @Autowired
//    private InventoryRepository inventoryRepository;
//    @Autowired
//    private FilmRepository filmRepository;
// 
//    @Autowired
//    private StoreRepository storeRepository;
// 
//    public List<InventoryDtO> getAllFilmInventories() {
//        List<Inventory> inventories = inventoryRepository.findAll();
//        return inventories.stream()
//                .map(this::convertToDto)
//                .collect(Collectors.toList());
//    }
// 
//    public List<InventoryDtO> getStoreInventoriesByFilm(Long filmid) {
//        List<Inventory> inventories = inventoryRepository.findByFilm_FilmId(filmid);
//        return inventories.stream()
//                .map(this::convertToDto)
//                .collect(Collectors.toList());
//    }
// 
//    public List<InventoryDtO> getFilmInventoriesByStore(Long storeId) {
//        List<Inventory> inventories = inventoryRepository.findByStore_StoreId(storeId);
//        return inventories.stream()
//                .map(this::convertToDto)
//                .collect(Collectors.toList());
//    }
// 
//    public InventoryDtO getFilmInventoryByStore(Long filmid, Long storeId) {
//        List<Inventory> inventories = inventoryRepository.findByFilm_FilmIdAndStore_StoreId(filmid, storeId);
//        return inventories.stream()
//                .map(this::convertToDto)
//                .findFirst()
//                .orElse(null);
//    }
// 
//    private InventoryDtO convertToDto(Inventory inventory) {
//        // Assuming Inventory has getFilm() and getStore() methods that return Film and Store objects respectively
//    	  Long filmId = inventory.getFilm() != null ? inventory.getFilm().getFilmid() : null;
//          Long storeId = inventory.getStore() != null ? inventory.getStore().getStoreId() : null;
//
//          return new InventoryDtO(
//              inventory.getInventoryId(),
//              filmId,
//              storeId,
//              inventory.getLastUpdate()
//        );
//    }
//
//	
//		 public Inventory addFilmToStore(InventoryDtO inventoryDTO) throws ResourceNotFoundException {
//		        // Ensure IDs are provided
//		        if (inventoryDTO.getFilmid() == null || inventoryDTO.getStoreId() == null) {
//		            throw new IllegalArgumentException("Film ID and Store ID must not be null");
//		        }
//
//		        // Fetch Film and Store entities
//		        Film film = filmRepository.findById(inventoryDTO.getFilmid())
//		                                  .orElseThrow(() -> new ResourceNotFoundException("Film not found"));
//		        Store store = storeRepository.findById(inventoryDTO.getStoreId())
//		                                     .orElseThrow(() -> new ResourceNotFoundException("Store not found"));
//
//		        // Create and save Inventory entity
//		        Inventory inventory = new Inventory();
//		        inventory.setFilm(film);
//		        inventory.setStore(store);
//		        inventory.setLastUpdate(inventoryDTO.getLastUpdate());
//
//		        return inventoryRepository.save(inventory);
//		    
//		    
//		
//	}
//}
package com.example.filmrental.service;
 
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.filmrental.dto.*;
import com.example.filmrental.exception.*;
import com.example.filmrental.repository.*;
import com.example.filmrental.model.*;
 
@Service
public class InventoryService {
 
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private FilmRepository filmRepository;
 
    @Autowired
    private StoreRepository storeRepository;
 
//    public String addFilmToStore(Long filmId, Long storeId) {
//        Inventory inventory = new Inventory();
//        Film film = new Film(); // You will need to retrieve Film and Store entities based on IDs
//        Store store = new Store(); // Ideally through respective services
//        inventory.setFilm(film);
//        inventory.setStore(store);
//        inventoryRepository.save(inventory);
//        return "Record Created Successfully";
//    }
//    public String addFilmToStore(Long filmId, Long storeId, LocalDateTime lastUpdate) throws EntityNotFoundException {
//        // Retrieve Film entity from the database using filmId
//        Film film = filmRepository.findById(filmId)
//            .orElseThrow(() -> new EntityNotFoundException("Film with ID " + filmId + " not found"));
//
//        // Retrieve Store entity from the database using storeId
//        Store store = storeRepository.findById(storeId)
//            .orElseThrow(() -> new EntityNotFoundException("Store with ID " + storeId + " not found"));
//
//        // Create new Inventory instance and set Film, Store, and Last Update
//        Inventory inventory = new Inventory();
//        inventory.setFilm(film);
//        inventory.setStore(store);
//        inventory.setLastUpdate(lastUpdate != null ? lastUpdate : LocalDateTime.now());
//
//        // Save Inventory to the database
//        inventoryRepository.save(inventory);
//
//        return "Record Created Successfully";
//    }
 
 
    public List<InventoryDTO> getAllFilmInventories() {
        List<Inventory> inventories = inventoryRepository.findAll();
        return inventories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
 
    public List<InventoryDTO> getStoreInventoriesByFilm(Long filmId) {
        List<Inventory> inventories = inventoryRepository.findByFilm_FilmId(filmId);
        return inventories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
 
    public List<InventoryDTO> getFilmInventoriesByStore(Long storeId) {
        List<Inventory> inventories = inventoryRepository.findByStore_StoreId(storeId);
        return inventories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
 
    public InventoryDTO getFilmInventoryByStore(Long filmId, Long storeId) {
        List<Inventory> inventories = inventoryRepository.findByFilm_FilmIdAndStore_StoreId(filmId, storeId);
        return inventories.stream()
                .map(this::convertToDto)
                .findFirst()
                .orElse(null);
    }
 
    private InventoryDTO convertToDto(Inventory inventory) {
        // Assuming Inventory has getFilm() and getStore() methods that return Film and Store objects respectively
    	  Integer filmId = inventory.getFilm() != null ? inventory.getFilm().getFilmId() : null;
          Long storeId = inventory.getStore() != null ? inventory.getStore().getStoreId() : null;

          return new InventoryDTO(
              inventory.getInventoryId(),
              filmId,
              storeId,
              inventory.getLastUpdate()
        );
    }

	
		 public Inventory addFilmToStore(InventoryDTO inventoryDTO) throws ResourceNotFoundException {
		        // Ensure IDs are provided
		        if (inventoryDTO.getFilmId() == null || inventoryDTO.getStoreId() == null) {
		            throw new IllegalArgumentException("Film ID and Store ID must not be null");
		        }

		        // Fetch Film and Store entities
		        Film film = filmRepository.findById(inventoryDTO.getFilmId())
		                                  .orElseThrow(() -> new ResourceNotFoundException("Film not found"));
		        Store store = storeRepository.findById(inventoryDTO.getStoreId())
		                                     .orElseThrow(() -> new ResourceNotFoundException("Store not found"));

		        // Create and save Inventory entity
		        Inventory inventory = new Inventory();
		        inventory.setFilm(film);
		        inventory.setStore(store);
		        inventory.setLastUpdate(inventoryDTO.getLastUpdate());

		        return inventoryRepository.save(inventory);
		    
		    
		
	}
}