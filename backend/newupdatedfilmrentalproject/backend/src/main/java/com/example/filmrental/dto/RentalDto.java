
package com.example.filmrental.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RentalDto {
	@NotNull
	@Positive
	private Long rentalId;
	
    @NotNull(message = "inventoryId is required")
    @Positive(message = "inventoryId must be positive")
    private Long inventoryId;

    @NotNull(message = "customerId is required")
    @Positive(message = "customerId must be positive")
    private Long customerId;

    @NotNull(message = "staffId is required")
    @Positive(message = "staffId must be positive")
    private Long staffId;

   private LocalDateTime returnDate;
    private LocalDateTime rentalDate;
    private LocalDateTime lastUpdate;

   
}
