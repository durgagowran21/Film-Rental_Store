package com.example.filmrental.service;
 
import java.time.LocalDate;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import java.util.stream.Collectors;
 
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.example.filmrental.dto.CustomerDto;
import com.example.filmrental.dto.ManagerSummaryDto;
import com.example.filmrental.dto.StaffDTO;
import com.example.filmrental.dto.StoreDto;
import com.example.filmrental.dto.StoreIdDTO;
import com.example.filmrental.exception.ResourceNotFoundException;
import com.example.filmrental.model.Address;
import com.example.filmrental.model.Customer;
import com.example.filmrental.model.Staff;
import com.example.filmrental.model.Store;
import com.example.filmrental.repository.AddressRepository;
import com.example.filmrental.repository.CustomerRepository;
import com.example.filmrental.repository.StaffRepository;
import com.example.filmrental.repository.StoreRepository;

import jakarta.transaction.Transactional;
 

 
@Service

public class StoreService  {

    @Autowired

    private StoreRepository storeRepository;

    @Autowired

    private AddressRepository addressRepository;

    @Autowired

    private StaffRepository staffRepository;

    @Autowired

    private CustomerRepository customerRepository;

    @Autowired

    private ModelMapper modelMapper;

   

    public List<Store> getAllStores() {

        return storeRepository.findAll();

    }


    public Store addStore(StoreDto storeCreateDTO) throws ResourceNotFoundException {

       Address address = addressRepository.findById(storeCreateDTO.getAddressID())

                .orElseThrow(() -> new ResourceNotFoundException("Address with ID " + storeCreateDTO.getAddress() + " Not Found"));
 
        Store store = new Store();

        store.setAddress(address);

        store.setLastUpdate(LocalDateTime.now());
 
        // Additional fields can be set here, like staff or customers

        return storeRepository.save(store);

    }

    

    public StoreDto assignAddressToStore(Long storeId, Long addressId) throws ResourceNotFoundException {

        // Fetch store by storeId

        Store store = storeRepository.findById(storeId)

            .orElseThrow(() -> new ResourceNotFoundException("Store Not Found With Id " + storeId));
 
        // Fetch address by addressId

        Address address = addressRepository.findById(addressId)

            .orElseThrow(() -> new ResourceNotFoundException("Address Not Found With Id " + addressId));
 
        // Update store with the new address

        store.setAddress(address);

        store.setLastUpdate(LocalDateTime.now());
 
        // Save the updated store

        Store updatedStore = storeRepository.save(store);
 
        // Convert to DTO and return

        return modelMapper.map(updatedStore, StoreDto.class);

    }
 
    


    public List<StoreDto> getStoresByCity(String cityName) throws ResourceNotFoundException {

        List<StoreDto> stores = storeRepository.findAll()

                .stream()

                .filter(store -> store.getAddress().getCity().getCityName().equalsIgnoreCase(cityName))

                .map(store -> modelMapper.map(store, StoreDto.class))

                .collect(Collectors.toList());
 
        if (stores.isEmpty()) {

            throw new ResourceNotFoundException("No Stores Found In City: " + cityName);

        }
 
        return stores;

    }



    public List<StoreIdDTO> getStoreIdsByCountry(String country) throws ResourceNotFoundException {

        List<StoreIdDTO> storeIdDTOs = storeRepository.findAll()

                .stream()

                .filter(store -> store.getAddress().getCity().getCountry().getCountry().equalsIgnoreCase(country))

                .map(store -> new StoreIdDTO(store.getStoreId(),store.getAddress().getPhone())) // Map only storeId to StoreIdDTO

                .collect(Collectors.toList());

        if (storeIdDTOs.isEmpty()) {

            throw new ResourceNotFoundException("No Stores Found In Country: " + country);

        }

        return storeIdDTOs;

    }
 
    

  

    public StoreDto getStoreByPhone(String phone) throws ResourceNotFoundException {

        Address address = addressRepository.findByPhone(phone);

        if (address == null) {

            throw new ResourceNotFoundException("Address With Phone Number " + phone + " Not Found.");

        }

        Store store = address.getStores().stream().findFirst().orElse(null);

        if (store == null) {

            throw new ResourceNotFoundException("Store Not Found For Address With Phone Number " + phone);

        }

        return modelMapper.map(store, StoreDto.class);

    }
 
    

 

    public String updatePhoneNumber(Long storeId, String phone) throws ResourceNotFoundException {

        Store store = storeRepository.findById(storeId)

            .orElseThrow(() -> new ResourceNotFoundException("Store With Id " + storeId + " Not Found"));
 
        Address address = store.getAddress();

        if (address == null) {

            throw new ResourceNotFoundException("No Address Associated With Store ID " + storeId);

        }
 
        address.setPhone(phone);

        address.setLastUpdate(LocalDateTime.now());
 
        addressRepository.save(address);
 
        return "Phone Number Updated Successfully To " + phone;

    }


   

    public List<Staff> getStaffByStoreId(Long storeId) throws ResourceNotFoundException {

        Store store = storeRepository.findById(storeId)

                .orElseThrow(() -> new ResourceNotFoundException("Store With ID " + storeId + " Not Found"));
 
        return store.getStaffList();

    }
 
    

    public List<CustomerDto> getCustomersByStoreId(Long storeId) throws ResourceNotFoundException {

        Store store = storeRepository.findById(storeId)

                .orElseThrow(() -> new ResourceNotFoundException("Store With ID " + storeId + " Not Found"));
 
        List<Customer> customers = customerRepository.findByStore_StoreId(storeId);

        if (customers.isEmpty()) {

            throw new ResourceNotFoundException("No Customers Found For Store With ID " + storeId);

        }
 
        return customers.stream()

                .map(customer -> modelMapper.map(customer, CustomerDto.class))

                .collect(Collectors.toList());

    }

//
//    @Transactional
//    public StoreDto assignManagerToStore(Long storeId, Long managerStaffId) throws ResourceNotFoundException {
//        Store store = storeRepository.findById(storeId)
//                .orElseThrow(() -> new ResourceNotFoundException("Store With ID " + storeId + " Not Found"));
//
//        Staff manager = staffRepository.findById(managerStaffId)
//                .orElseThrow(() -> new ResourceNotFoundException("Staff (manager) With ID " + managerStaffId + " Not Found"));
//
//        store.setManager(manager);
//        store.setLastUpdate(LocalDateTime.now());
//
//        Store saved = storeRepository.save(store);
//        return modelMapper.map(saved, StoreDto.class);
//    }
//
//    @Transactional
//    public StaffDTO getManagerOfStore(Long storeId) throws ResourceNotFoundException {
//        Store store = storeRepository.findById(storeId)
//                .orElseThrow(() -> new ResourceNotFoundException("Store With ID " + storeId + " Not Found"));
//
//        Staff manager = store.getManager();
//        if (manager == null) {
//            throw new ResourceNotFoundException("No manager assigned for Store ID " + storeId);
//        }
//        return modelMapper.map(manager, StaffDTO.class);
//    }
//
//    @Transactional
//    public List<ManagerSummaryDto> getAllStoreManagers() {
//        // naive approach: load all stores, map manager + store + address
//        return storeRepository.findAll().stream()
//                .map(store -> {
//                    Staff mgr = store.getManager();
//                    if (mgr == null) return null;
//
//                    Address addr = store.getAddress();
//                    ManagerSummaryDto dto = new ManagerSummaryDto();
//                    dto.setStoreId(store.getStoreId());
//                    dto.setManagerStaffId(mgr.getStaffId());
//                    dto.setManagerFirstName(mgr.getFirstName());
//                    dto.setManagerLastName(mgr.getLastName());
//                    dto.setManagerEmail(mgr.getEmail());
//                    dto.setManagerPhone(addr != null ? addr.getPhone() : null); // or mgr’s phone if you have it
//                    if (addr != null) {
//                        dto.setStorePhone(addr.getPhone());
//                        if (addr.getCity() != null) {
//                            dto.setCity(addr.getCity().getCityName());
//                            if (addr.getCity().getCountry() != null) {
//                                dto.setCountry(addr.getCity().getCountry().getCountry());
//                            }
//                        }
//                        dto.setAddressLine1(addr.getAddress());
//                        dto.setAddressLine2(addr.getAddress2());
//                    }
//                    return dto;
//                })
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
//    }
}


 