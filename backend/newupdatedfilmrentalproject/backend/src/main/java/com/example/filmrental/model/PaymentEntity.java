package com.example.filmrental.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "payment")
@Data
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long paymentId;
    @ManyToOne
    @JoinColumn(name="customer_id",nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name="staff_id",nullable = false)
    private Staff staff;

    @ManyToOne
    @JoinColumn(name="rental_id",nullable = false)
    private RentalEntity rental;


    @Column(nullable = false, columnDefinition = "Double(5,2)")
    private Double amount;

    @Column(nullable = false)
    private LocalDateTime paymentDate;

    @Column(nullable = false)
    private LocalDateTime lastUpdate;

  
    @PrePersist
    public void onCreate() {
        if (this.paymentDate == null) {
            this.paymentDate = LocalDateTime.now();
        }
        this.lastUpdate = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.lastUpdate = LocalDateTime.now();
    }
}
