package com.example.filmrental;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FilmrentalApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilmrentalApplication.class, args);
		//System.out.println("Cloning occurs");
			}
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}


}
