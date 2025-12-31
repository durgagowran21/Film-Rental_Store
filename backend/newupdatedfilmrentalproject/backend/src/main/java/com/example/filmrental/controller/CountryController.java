package com.example.filmrental.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.filmrental.dto.AddressDto;
import com.example.filmrental.model.Country;
import com.example.filmrental.service.CountryService;
@RestController
@RequestMapping("/api/country")
public class CountryController {
	@Autowired
	private CountryService countryservice;
	@PostMapping("/post")

	public ResponseEntity<String> createCountry(@RequestBody Country country) {

		countryservice.createcountry(country);

		return new ResponseEntity<>("Record Created Successfully", HttpStatus.CREATED);

	}
 

}
