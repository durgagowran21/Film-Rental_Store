package com.example.filmrental.repository;

import java.util.List;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.filmrental.model.Customer;
 
 

@Repository
public interface CustomerRepository extends JpaRepository <Customer, Long> {


	List<Customer> findByFirstName(String firstName);

	List<Customer> findByLastName(String lastName);

	List<Customer> findByAddressPhone(String phone);

    List<Customer> findByEmail(String email);

    List<Customer> findByAddressCityCityName(String cityName);

    List<Customer> findByAddressCityCountryCountry(String countryname);

    List<Customer> findByActive(boolean active);

    List<Customer> findByStore_StoreId(Long storeId); 

}
 


