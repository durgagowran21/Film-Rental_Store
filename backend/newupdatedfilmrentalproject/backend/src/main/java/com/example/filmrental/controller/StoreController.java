package com.example.filmrental.controller;


import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
 
import com.example.filmrental.dto.*;


import com.example.filmrental.exception.*;

import com.example.filmrental.model.*;

import com.example.filmrental.service.*;
 
import jakarta.validation.Valid;
 
 

@RestController

@RequestMapping("/api/stores")

@Valid

public class StoreController {

    @Autowired

   StoreService storeService;


    //Get all Store Info

    @GetMapping("/all")

    public List<Store> getAllStores() {

        return storeService.getAllStores();

    }


    //Add New Store 

    @PostMapping("/add")

    public ResponseEntity<Store> addStore(@Validated @RequestBody StoreDto storecreateDTO) throws ResourceNotFoundException {

        Store newStore = storeService.addStore(storecreateDTO);

        return new ResponseEntity<>(newStore, HttpStatus.CREATED);

    }

 
    //Assign address to store

    @PutMapping("/{storeId}/address/{addressId}")

    public ResponseEntity<StoreDto> assignAddressToStore(

        @PathVariable Long storeId, 

        @PathVariable Long addressId) throws ResourceNotFoundException {

        StoreDto updatedStore = storeService.assignAddressToStore(storeId, addressId);

        return ResponseEntity.ok(updatedStore);

    }


    //Get Store By City

    @GetMapping("/city/{city}")

    public List<StoreDto> getStoresByCity(@PathVariable String city) throws ResourceNotFoundException{

        return storeService.getStoresByCity(city);

    }


    //Getting Store By Country

    @GetMapping("/country/{country}")

    public List<StoreIdDTO> getStoresByCountry(@PathVariable String country) throws ResourceNotFoundException {

        return storeService.getStoreIdsByCountry(country);

    }


    //Getting Store By Phone Number

    @GetMapping("/phone/{phone}")

    public ResponseEntity<StoreDto> getStoreByPhone(@PathVariable("phone") String phone) throws ResourceNotFoundException {

        StoreDto store = storeService.getStoreByPhone(phone);

        return ResponseEntity.ok(store);

    }


    //Update the phone number

    @PutMapping("/update/{storeId}/{phone}")

    public ResponseEntity<String> updateStorePhoneNumber(

            @PathVariable("storeId") Long storeId,

            @PathVariable("phone") String phone) throws InvalidInputException {
 
        try {

            String message = storeService.updatePhoneNumber(storeId, phone);

            return ResponseEntity.ok(message);

        } catch (ResourceNotFoundException ex) {

            return ResponseEntity.badRequest().body(ex.getMessage());

        }

    }


    //Get Staff List of the Store Id

    @GetMapping("/staff/{storeId}")

    public List<Staff> getStaffByStoreId(@PathVariable long storeId) throws ResourceNotFoundException {

        return storeService.getStaffByStoreId((long) storeId);

    }


    //Get Customer By Store Id

    @GetMapping("/customer/{storeId}")

    public ResponseEntity<List<CustomerDto>> getCustomersByStore(@PathVariable Long storeId) throws ResourceNotFoundException {

        List<CustomerDto> customers = storeService.getCustomersByStoreId(storeId);

        return ResponseEntity.ok(customers);

    }
}
//
//     @PutMapping("/{storeId}/manager/{managerStaffId}")
//    public ResponseEntity<StoreDto> assignManagerToStore(@PathVariable Long storeId,
//                                                         @PathVariable Long managerStaffId) throws ResourceNotFoundException {
//        StoreDto dto = storeService.assignManagerToStore(storeId, managerStaffId);
//        return ResponseEntity.ok(dto);
//    }
//
//    @GetMapping("/manager/{storeId}")
//    public ResponseEntity<?> getManagerOfStore(@PathVariable Long storeId) throws ResourceNotFoundException {
//        return ResponseEntity.ok(storeService.getManagerOfStore(storeId));
//
//}
//
//    @GetMapping("/managers")
//    public ResponseEntity<List<ManagerSummaryDto>> getAllStoreManagers() {
//        return ResponseEntity.ok(storeService.getAllStoreManagers());
//    }
//}


 