package com.example.filmrental.dto;



import lombok.*;
import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor 
public class ActorDto {
	
	@NotNull
	private Integer id;
	@NotBlank
	private String firstName;
	@NotBlank
	private String lastName;

	

}

