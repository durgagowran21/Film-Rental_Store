package com.example.filmrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.filmrental.model.City;
import com.example.filmrental.model.Country;

public interface CountryRepository extends JpaRepository<Country, Integer> {

}
