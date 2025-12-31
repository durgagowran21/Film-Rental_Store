
package com.example.filmrental.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long storeId;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "address_id")
    private Address address;
    @OneToMany(mappedBy = "store")
    @JsonManagedReference
    private List<Customer> customers;
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Staff> staffList = new ArrayList<>();
//    @ManyToOne
//    @JoinColumn(name = "manager_staff_id")
//    private Staff manager;                
    private LocalDateTime lastUpdate;
}
