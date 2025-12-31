package com.example.filmrental.model;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.example.filmrental.dto.StoreDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data
@Entity
public class Staff {
	@Schema(accessMode = AccessMode.READ_ONLY)
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long staffId;
	@NotBlank
	@Size(max= 45)
	@Column(name="first_Name",nullable=false)
	private String firstName;
	
	@NotBlank
	@Size(max= 45)
	@Column(name="last_name",nullable=false)
	private String lastName;
	
//	@Lob
//	@Column
//	private byte[] picture;
	

	@Email
	@Size(max = 50)
	@Column(name = "email", length = 50)
	private String email;
	
	@Column(name = "active", nullable = false)
	private Boolean active;
	

	 @NotBlank
	 @Size(max = 16)
	 @Column(name = "username",  nullable = false, unique = true)
	 private String username;

	 @NotBlank
	 @Size(max = 40)
	 @Column(name = "password",  nullable = false)
	 private String password;

	 
	@Column(name = "last_update", nullable = false)
	private LocalDateTime lastupdate=LocalDateTime.now();
	
	@ManyToOne
	//@JoinColumn(name="Store_id" , nullable=false)
	@JsonBackReference
	private Store store;
	
//	@OneToMany(mappedBy="staff")
//	private Set<Rental> rentals=new HashSet<>();
	
//	@OneToMany(mappedBy="staff")
//	private Set<Payment> payments = new HashSet<>();
	@Column(name="country")
	private String country;
	
	 @PrePersist @PreUpdate
	    public void touch() {
	        this.lastupdate = LocalDateTime.now();
	    }
	 @ManyToOne
	 @JoinColumn(name = "address_id", nullable = false)
	 private Address address;
	
	}
	

