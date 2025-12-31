
package com.example.filmrental.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "country")
@Getter
@Setter

public class Country {
	
	@Schema(accessMode = AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id", nullable = false)
    private Integer countryId; 

    @Size(max = 50)
    @Column(name = "country", nullable = false, length = 50)
    private String country;

    @UpdateTimestamp
    @Column(name = "last_update", nullable = false)
    private Instant lastUpdate; 
    

    // Back-reference to cities
    @OneToMany(mappedBy = "country")
    @JsonManagedReference
    @JsonIgnore
    private List<City> cities;
}
