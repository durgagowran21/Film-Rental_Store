package com.example.filmrental.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDTO {

	@Schema(accessMode = AccessMode.READ_ONLY)
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long inventoryId;
	@NotNull
    private Integer filmId;         // ID of the Film
    @NotNull
	private Long storeId;        // ID of the Store
    private LocalDateTime lastUpdate;
}
