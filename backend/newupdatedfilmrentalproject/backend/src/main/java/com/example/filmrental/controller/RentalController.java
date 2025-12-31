package com.example.filmrental.controller;
 
import java.time.LocalDateTime;

import java.util.List;

import java.util.Map;
 
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
 
import com.example.filmrental.dto.*;

import com.example.filmrental.exception.*;

import com.example.filmrental.service.*;
 
@RestController

@RequestMapping("/api/rentals")
 
public class RentalController {

//

//    @PostMapping("/post")

//    public String addRental(@RequestBody Rental rental) {

//        rentalService.addRental(rental);

//        return "Record Created Successfully";

//    }
 
	 @Autowired

	    private RentalService rentalService;
 
	    @PostMapping("/add")

	    public ResponseEntity<String> addRental(@RequestBody RentalDto rentalDTO) {

	        rentalService.addRental(rentalDTO);

	        return ResponseEntity.ok("Record Created Successfully");

	    }

	    @GetMapping("/customer/{id}")

	    public ResponseEntity<List<RentalDto>> getRentalsByCustomer(@PathVariable Long id) throws ResourceNotFoundException {

	        List<RentalDto> rentals = rentalService.getRentalsByCustomerId(id);

	        return ResponseEntity.ok(rentals);

	    }

	    @GetMapping("/toptenfilms")

	    public ResponseEntity<List<RentalDto>> getTopTenFilms() {

	        List<RentalDto> topTenFilms = rentalService.getTopTenFilms();

	        return ResponseEntity.ok(topTenFilms);

	    }

	    @GetMapping("/toptenfilms/store/{id}")

	    public ResponseEntity<List<RentalDto>> getTopTenFilmsByStore(@PathVariable Long id) {

	        List<RentalDto> topTenFilmsByStore = rentalService.getTopTenFilmsByStore(id);

	        return ResponseEntity.ok(topTenFilmsByStore);

	    }

	    @GetMapping("/due/store/{id}")

	    public ResponseEntity<Map<Long,String>> getPendingReturnsByStore(@PathVariable Long id) {

	        Map<Long, String> pendingReturns = rentalService.getCustomersWithPendingReturnsByStore(id);

	        return ResponseEntity.ok(pendingReturns);

	    }

	    @PostMapping("/update/returndate/{id}")

	    public ResponseEntity<RentalDto> updateReturnDate(@PathVariable Long id, @RequestBody LocalDateTime returnDate) throws ResourceNotFoundException {

	        RentalDto updatedRental = rentalService.updateReturnDate(id, returnDate);

	        return ResponseEntity.ok(updatedRental); 

	    }
 
}

 