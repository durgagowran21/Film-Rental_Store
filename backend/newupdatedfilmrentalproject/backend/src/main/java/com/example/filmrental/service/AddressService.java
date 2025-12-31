package com.example.filmrental.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.filmrental.dto.AddressDto;
import com.example.filmrental.dto.CustomerDto;
import com.example.filmrental.model.Address;
import com.example.filmrental.model.Customer;
import com.example.filmrental.repository.AddressRepository;
import com.example.filmrental.repository.CustomerRepository;

@Service
public class AddressService {
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    ModelMapper modelMapper;

	
	public AddressDto createAddress(AddressDto addressDTO) {

       Address address = modelMapper.map(addressDTO, Address.class);

        Address savedaddress = addressRepository.save(address);

        return modelMapper.map(savedaddress, AddressDto.class);

    }

}
