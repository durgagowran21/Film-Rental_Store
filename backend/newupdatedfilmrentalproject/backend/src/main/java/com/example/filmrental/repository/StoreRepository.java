package com.example.filmrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.filmrental.model.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {

}
