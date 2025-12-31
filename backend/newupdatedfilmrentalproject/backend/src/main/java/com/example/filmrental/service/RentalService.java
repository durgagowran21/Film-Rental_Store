package com.example.filmrental.service;
 
import java.time.LocalDateTime;

import java.util.HashMap;

import java.util.List;

import java.util.Map;

import java.util.stream.Collectors;
 
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
 
import com.example.filmrental.dto.*;

import com.example.filmrental.exception.*;

import com.example.filmrental.repository.*;

import com.example.filmrental.model.*;
 
@Service

public class RentalService  {  // Implementing the interface
 
    @Autowired

    private RentalRepository rentalRepository;
 
    private ModelMapper modelMapper = new ModelMapper();
 
    public RentalDto addRental(RentalDto rentalDTO) {

        RentalEntity rental = modelMapper.map(rentalDTO, RentalEntity.class);

        rental.setRentalDate(LocalDateTime.now());

        rental.setLastUpdate(LocalDateTime.now());

        RentalEntity savedRental = rentalRepository.save(rental);

        return modelMapper.map(savedRental, RentalDto.class);

    }

    public List<RentalDto> getRentalsByCustomerId(Long customerId) throws ResourceNotFoundException {

        List<RentalEntity> rentals = rentalRepository.findAll()

            .stream()

            .filter(rental -> rental.getCustomer().getCustomerId().equals(customerId))

            .collect(Collectors.toList());
 
        if (rentals.isEmpty()) {

            throw new ResourceNotFoundException("No rentals found for customer id: " + customerId);

        }
 
        return rentals.stream()

            .map(rental -> modelMapper.map(rental, RentalDto.class))

            .collect(Collectors.toList());

    }

    public List<RentalDto> getTopTenFilms() {

        // Logic for finding the top 10 most rented films

        List<RentalEntity> rentals = rentalRepository.findAll().stream()

            .sorted((r1, r2) -> r2.getRentalDate().compareTo(r1.getRentalDate()))  // Sort by rental date

            .limit(10)

            .collect(Collectors.toList());
 
        return rentals.stream()

            .map(rental -> modelMapper.map(rental, RentalDto.class))

            .collect(Collectors.toList());

    }

    public List<RentalDto> getTopTenFilmsByStore(Long storeId) {

        // Logic for finding the top 10 most rented films by store

        List<RentalEntity> rentals = rentalRepository.findAll().stream()

            .filter(rental -> rental.getInventory().getStore().getStoreId().equals(storeId))

            .sorted((r1, r2) -> r2.getRentalDate().compareTo(r1.getRentalDate()))  // Sort by rental date

            .limit(10)

            .collect(Collectors.toList());
 
        return rentals.stream()

            .map(rental -> modelMapper.map(rental, RentalDto.class))

            .collect(Collectors.toList());

    }

    public Map<Long,String> getCustomersWithPendingReturnsByStore(Long storeId) {

        // Logic to get customers with pending returns by store

        List<RentalEntity> rentals = rentalRepository.findAll().stream()

            .filter(rental -> rental.getReturnDate() == null && rental.getInventory().getStore().getStoreId().equals(storeId))

            .collect(Collectors.toList());

        Map<Long,String> customerDetails = new HashMap<>();

        for(RentalEntity r : rentals) {

        	customerDetails.put(r.getCustomer().getCustomerId(),r.getCustomer().getFirstName());

        }

//        return rentals.stream()

//            .map(rental -> modelMapper.map(rental, RentalDTO.class))

//            .collect(Collectors.toList());

        return customerDetails;

    }

    public RentalDto updateReturnDate(Long rentalId, LocalDateTime returnDate) throws ResourceNotFoundException {

        RentalEntity rental = rentalRepository.findById(rentalId)

            .orElseThrow(() -> new ResourceNotFoundException("Rental not found with id: " + rentalId));

        rental.setReturnDate(returnDate);

        rental.setLastUpdate(LocalDateTime.now());

        RentalEntity updatedRental = rentalRepository.save(rental);

        return modelMapper.map(updatedRental, RentalDto.class);

    }
 
}

 