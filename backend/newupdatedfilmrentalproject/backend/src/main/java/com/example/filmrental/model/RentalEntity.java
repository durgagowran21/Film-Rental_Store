
package com.example.filmrental.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "rental")
@Data
public class RentalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_id", length = 22)
    private Long rentalId;

    @Column(name = "rental_date", nullable = false)
    private LocalDateTime rentalDate;

    @ManyToOne
    @JoinColumn(name="inventory_id",nullable = false)
    private Inventory inventory;

    @ManyToOne
    @JoinColumn(name="customer_id",nullable = false)
    private Customer customer;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @ManyToOne
    @JoinColumn(name="staff_id",nullable = false)
    private Staff staff;
    
    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    
}

