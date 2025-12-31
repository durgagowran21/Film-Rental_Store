package com.example.filmrental.dto;
 
import java.time.LocalDateTime;
 
import com.example.filmrental.model.*;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    
	@Schema(accessMode = AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id", nullable = false)
    private Long addressId;
    private String address;
    private String address2;
    private String district;
    @NotNull
    private Integer cityId;
    private String postalCode;
    private String phone;
 
}
 
 