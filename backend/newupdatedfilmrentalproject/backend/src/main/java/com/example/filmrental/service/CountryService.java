package com.example.filmrental.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.filmrental.dto.AddressDto;
import com.example.filmrental.model.Address;
import com.example.filmrental.model.Country;
import com.example.filmrental.repository.CountryRepository;

@Service
public class CountryService {
	@Autowired
	private CountryRepository countryrepo;
//	@Autowired
//    ModelMapper modelMapper;

	
	public Country createcountry(Country country) {


        Country savedcountry = countryrepo.save(country);

        return savedcountry;

    }

}
