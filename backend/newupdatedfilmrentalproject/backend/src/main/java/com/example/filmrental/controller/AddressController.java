package com.example.filmrental.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.filmrental.dto.AddressDto;
import com.example.filmrental.dto.CustomerDto;
import com.example.filmrental.service.AddressService;
@RestController
@RequestMapping("/api/address")
public class AddressController 
{
	@Autowired
	private AddressService addressService;
	@PostMapping("/post")

	public ResponseEntity<String> createAddress(@RequestBody AddressDto addressDTO) {

		addressService.createAddress(addressDTO);

		return new ResponseEntity<>("Record Created Successfully", HttpStatus.CREATED);

	}
 

}
