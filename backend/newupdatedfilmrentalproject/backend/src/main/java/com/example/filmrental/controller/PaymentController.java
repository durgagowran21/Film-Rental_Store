package com.example.filmrental.controller;
 
import java.time.LocalDate;

import java.time.LocalDateTime;

import java.util.Map;
 
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
 
import com.example.filmrental.dto.*;

import com.example.filmrental.exception.*;


import com.example.filmrental.service.*;
 
@RestController

@RequestMapping("/api/payment")

public class PaymentController {
 
    @Autowired

    private PaymentService paymentService;
 
    @PutMapping("/add")

    public ResponseEntity<String> addPayment(@RequestBody PaymentDTO paymentDTO) throws InvalidInputException {

        if (paymentDTO.getAmount() <= 0) {

            throw new InvalidInputException("Payment amount must be greater than zero.");

        }

        paymentService.addPayment(paymentDTO);

        return ResponseEntity.ok("Record Created Successfully");

    }

    @GetMapping("/revenue/datewise")

    public ResponseEntity<Map<LocalDate, Double>> getRevenueDatewise() {

        Map<LocalDate, Double> revenue = paymentService.getCumulativeRevenueDatewise();

        return ResponseEntity.ok(revenue);

    }
 
    @GetMapping("/revenue/datewise/store/{id}")

    public ResponseEntity<Map<LocalDate, Double>> getRevenueByStoreDatewise(@PathVariable Long id) throws ResourceNotFoundException {

        Map<LocalDate, Double> revenue = paymentService.getCumulativeRevenueByStoreDatewise(id);

        if (revenue.isEmpty()) {

            throw new ResourceNotFoundException("No revenue found for the store ID: " + id);

        }

        return ResponseEntity.ok(revenue);

    }
 
    @GetMapping("/revenue/filmwise")

    public ResponseEntity<Map<String, Double>> getRevenueFilmwise() {

        Map<String, Double> revenue = paymentService.getCumulativeRevenueFilmwise();

        return ResponseEntity.ok(revenue);

    }
 
    @GetMapping("/revenue/film/{id}")

    public ResponseEntity<Map<String, Double>> getRevenueByFilmStorewise(@PathVariable Long id) throws ResourceNotFoundException {

        Map<String, Double> revenue = paymentService.getCumulativeRevenueByFilmStorewise(id);

        if (revenue.isEmpty()) {

            throw new ResourceNotFoundException("No revenue found for the film ID: " + id);

        }

        return ResponseEntity.ok(revenue);

    }
 
    @GetMapping("/revenue/films/store/{id}")

    public ResponseEntity<Map<String, Double>> getRevenueFilmsByStore(@PathVariable Long id) throws ResourceNotFoundException {

        Map<String, Double> revenue = paymentService.getCumulativeRevenueFilmsByStore(id);

        if (revenue.isEmpty()) {

            throw new ResourceNotFoundException("No revenue found for the store ID: " + id);

        }

        return ResponseEntity.ok(revenue);

    }
 
    // Exception handling within the controller

    @ExceptionHandler(InvalidInputException.class)

    public ResponseEntity<String> handleInvalidInputException(InvalidInputException ex) {

        return new ResponseEntity<>("Invalid input: " + ex.getMessage(), HttpStatus.BAD_REQUEST);

    }
 
    @ExceptionHandler(ResourceNotFoundException.class)

    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {

        return new ResponseEntity<>("Error: " + ex.getMessage(), HttpStatus.NOT_FOUND);

    }
 
    @ExceptionHandler(Exception.class)

    public ResponseEntity<String> handleGenericException(Exception ex) {

        return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

    }
 
}

 