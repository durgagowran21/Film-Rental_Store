package com.example.filmrental.repository;
 
import java.util.List;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
import com.example.filmrental.model.*;
 
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    // Query to find Inventory by the Film's ID
    List<Inventory> findByFilm_FilmId(Long filmId);
    // Query to find Inventory by the Store's ID
    List<Inventory> findByStore_StoreId(Long storeId);
    // Query to find Inventory by both Film ID and Store ID
    List<Inventory> findByFilm_FilmIdAndStore_StoreId(Long filmId, Long storeId);
}

