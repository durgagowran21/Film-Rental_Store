package com.example.filmrental.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.filmrental.dto.CityDto;
import com.example.filmrental.model.City;
import com.example.filmrental.model.Country;
import com.example.filmrental.repository.CityRepository;
import com.example.filmrental.repository.CountryRepository;
@Service
public class CityService {
	
	@Autowired
	private CityRepository cityrepo;
	@Autowired
    ModelMapper modelMapper;

	
	public CityDto createcity(CityDto cityDTO) {

       City city = modelMapper.map(cityDTO,City.class);

        City savedcity = cityrepo.save(city);

        return modelMapper.map(savedcity, CityDto.class);

    }

}
