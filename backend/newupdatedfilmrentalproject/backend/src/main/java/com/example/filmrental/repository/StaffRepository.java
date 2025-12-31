package com.example.filmrental.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.filmrental.model.Staff;

public interface StaffRepository extends JpaRepository<Staff, Long> {

	

	 boolean existsByUsername(String username);
	  Optional<Staff> findByUsername(String username);

	 
	  @Query("SELECT s FROM Staff s WHERE LOWER(s.lastName) = LOWER(:lastName)")
	  List<Staff> findByLastName(@Param("lastName") String lastName);

	  @Query("SELECT s FROM Staff s WHERE LOWER(s.firstName) = LOWER(:firstName)")
	  List<Staff> findByFirstName(@Param("firstName") String firstName);

	  @Query("SELECT s FROM Staff s WHERE LOWER(s.email) = LOWER(:email)")
	  List<Staff> findOneByEmail(@Param("email") String email);
	  
	   @Query("SELECT s FROM Staff s WHERE LOWER(s.address.city) = LOWER(:city)")
	  List<Staff> findOneByCity(@Param("city") String city);
	   
	   @Query("SELECT s FROM Staff s WHERE LOWER(s.country) = LOWER(:country)")
		  List<Staff> findOneByCountry(@Param("country") String country);
	   
	   @Query("SELECT s FROM Staff s WHERE LOWER(s.address.phone) = LOWER(:phone)")
   List<Staff> findOneByPhone(@Param("phone") String phone);

	   
}