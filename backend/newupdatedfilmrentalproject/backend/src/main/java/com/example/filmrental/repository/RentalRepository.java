
package com.example.filmrental.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.filmrental.model.RentalEntity;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<RentalEntity, Long> {
}