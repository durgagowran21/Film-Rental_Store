package com.example.filmrental.dto;
 
import java.time.LocalDateTime;

import java.util.List;
 
import com.example.filmrental.model.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.Valid;

import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.PastOrPresent;

import lombok.AllArgsConstructor;

import lombok.Data;

import lombok.NoArgsConstructor;
 
@Data

@AllArgsConstructor

@NoArgsConstructor

public class StoreDto {
 

	@Schema(accessMode = AccessMode.READ_ONLY)
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;
    @JsonIgnore
	@Valid
    private Address address;
	@NotNull
	@Valid
	private long addressID;
    private LocalDateTime lastUpdate;
	@JsonIgnore
	@Valid
    private List<Customer> customers;
	@Valid
	@JsonIgnore
    private List<Staff> staff_list;
 
    

}
 