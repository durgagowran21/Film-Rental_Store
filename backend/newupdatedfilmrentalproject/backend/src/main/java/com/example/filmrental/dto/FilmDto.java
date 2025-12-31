package com.example.filmrental.dto;



import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

//package com.example.filmstore.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
public class FilmDto {
	@Schema(accessMode = AccessMode.READ_ONLY)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer filmId;
  @NotBlank @Size(max=255) private String title;
  private String description;
  @Min(1900) @Max(2100) private Integer releaseYear; 
  @NotNull private Long languageId;
  @Min(value=1)
  private Integer rentalDuration;
  @Min(value = 0) 
  private Double rentalRate;
  @Min(0)
  private Integer length;
  @Min(value = 0)
  private Integer replacementCost;
  @Min(value = 0)
  private Integer rating;
  @Size(max = 500)
  private String specialFeatures;
}
