package com.example.filmrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.filmrental.model.City;

public interface CityRepository extends JpaRepository<City, Integer> {

}
