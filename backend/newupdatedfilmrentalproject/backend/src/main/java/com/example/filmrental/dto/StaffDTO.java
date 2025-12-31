package com.example.filmrental.dto;

import java.time.LocalDateTime;

import com.example.filmrental.model.Address;
import com.example.filmrental.model.Store;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StaffDTO {
	
	@Schema(accessMode = AccessMode.READ_ONLY)
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long staffId;
    @NotBlank 
    @Size(max = 45)
    private String firstName;
    @NotBlank 
    @Size(max = 45)
    private String lastName;
    @NotNull
    private Long addressId;
    @Email
    private String email;
    @NotNull
    private Long storeId;
    private Boolean active;
    @NotBlank 
    @Size(max = 16)
    private String username;
    @NotBlank
    @Size(max = 40)
    private String password;
    private LocalDateTime lastUpdate;

	
}
