package com.example.filmrental.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.filmrental.dto.CityDto;
import com.example.filmrental.service.CityService;
import com.example.filmrental.service.CountryService;

@RestController
@RequestMapping("/api/city")
public class CityController {
	@Autowired
	private CityService cityservice;
	@PostMapping("/post")

	public ResponseEntity<String> createCity(@RequestBody CityDto cityDTO) {

		cityservice.createcity(cityDTO);

		return new ResponseEntity<>("Record Created Successfully", HttpStatus.CREATED);

	}

}
