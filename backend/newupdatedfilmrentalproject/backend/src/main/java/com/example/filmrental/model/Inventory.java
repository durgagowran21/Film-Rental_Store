//package com.example.filmrental.model;
//
//import java.time.LocalDateTime;
//
//import com.example.filmrental.model.*;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@Entity
//@NoArgsConstructor
//@AllArgsConstructor
//public class Inventory {
//	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long inventoryId;
//	
//	@ManyToOne
//	@JoinColumn(name = "film_id", nullable = false)
//	private Film film;
//    //private Long filmId;
//	
//	@ManyToOne
//    @JoinColumn(name = "store_id", nullable = false)
//	private Store store;
//    //private Long storeId;
//
//    @Column(name = "last_update", nullable = false)
//    private LocalDateTime lastUpdate = LocalDateTime.now();
//
////	@ManyToOne
////	@JoinColumn(name="film_id")
////	private Film film;
////	
////	public Film getFilm() {
////		return film;
////	}
//	
//	
//	
//	
//}
package com.example.filmrental.model;
 
 
import java.time.LocalDateTime;
 
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Entity
@Table(name = "inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {
 
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long inventoryId;
 
    @ManyToOne
    @JoinColumn(name = "film_id")  // Correct field name
    private Film film;
 
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
 
    private LocalDateTime lastUpdate;
}
 
