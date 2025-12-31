
package com.example.filmrental.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "customer")
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @ManyToOne( optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    @JsonBackReference
    private Store store;

    @ManyToOne(optional = false)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "active", nullable = false)
    private Boolean active = Boolean.TRUE;

    @CreationTimestamp
    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDate createDate;

    @UpdateTimestamp
    @Column(name = "last_update", nullable = false)
    private Instant lastUpdate;
}
