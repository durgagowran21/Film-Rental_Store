
package com.example.filmrental.dto;

import jakarta.validation.constraints.NotNull;


import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentDTO {
	@NotNull
	private Long paymentId;

    @NotNull
    private Long customerId;

    @NotNull
    private Long staffId;

    private Long rentalId;

    @NotNull
    @Positive
    private Double amount;          
    private LocalDateTime paymentDate;
    private LocalDateTime lastUpdate;

    
}
