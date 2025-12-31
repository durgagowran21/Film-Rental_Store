
package com.example.filmrental.dto;

import java.time.LocalDateTime;

import com.example.filmrental.model.Address;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerDto {
	@Schema(accessMode = AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;
    private String firstName;
    private String lastName;
    @Email
    private String email;
    private Boolean active;
    @NotNull
    private Long addressId;
    @NotNull
    private Long storeId;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdate;

    
}
