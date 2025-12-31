
package com.example.filmrental.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;

@Entity
@Table(name = "city")
@Getter
@Setter

public class City {
	
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_id", nullable = false)
    private Integer cityId; // 

    @Size(max = 50)
    @Column(name = "city", nullable = false, length = 50)
    private String cityName;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    @JsonBackReference
    private Country country; // FK to country.country_id

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    @PrePersist
    @PreUpdate
    private void touchLastUpdate() {
        this.lastUpdate = LocalDateTime.now();
    }
}
