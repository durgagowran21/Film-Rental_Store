package com.example.filmrental.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.filmrental.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long>
{
	Address findByPhone(String phone);

	//Optional<Address> findById(Address address);

}
