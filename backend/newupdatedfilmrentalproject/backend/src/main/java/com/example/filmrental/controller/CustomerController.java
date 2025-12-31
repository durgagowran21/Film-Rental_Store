package com.example.filmrental.controller;

import java.util.List; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.filmrental.dto.CustomerDto;
import com.example.filmrental.exception.ResourceNotFoundException;
import com.example.filmrental.model.Store;
import com.example.filmrental.service.CustomerService;

 
@RestController

@RequestMapping("/api/customers")


public class CustomerController {

	@Autowired

	CustomerService customerService;
 
	@PostMapping("/post")

	public ResponseEntity<String> createCustomer(@RequestBody CustomerDto customerDTO) {

		customerService.createCustomer(customerDTO);

		return new ResponseEntity<>("Record Created Successfully", HttpStatus.CREATED);

	}
 
	@GetMapping("/lastname/{lastName}")

	public ResponseEntity<List<CustomerDto>> getCustomersByLastName(@PathVariable String lastName) throws ResourceNotFoundException {

		return new ResponseEntity<>(customerService.getCustomersByLastName(lastName), HttpStatus.OK);

	}
 
	@GetMapping("/firstname/{firstName}")

	public ResponseEntity<List<CustomerDto>> getCustomersByFirstName(@PathVariable String firstName) throws ResourceNotFoundException {

		return new ResponseEntity<>(customerService.getCustomersByFirstName(firstName), HttpStatus.OK);

	}
 
	@GetMapping("/email/{email}")

	public ResponseEntity<List<CustomerDto>> getCustomersByEmail(@PathVariable String email) throws ResourceNotFoundException {

		return new ResponseEntity<>(customerService.getCustomersByEmail(email), HttpStatus.OK);

	}
 
	@GetMapping("/active")

	public ResponseEntity<List<CustomerDto>> getActiveCustomers() {

		return new ResponseEntity<>(customerService.getActiveCustomers(), HttpStatus.OK);

	}
 
	@GetMapping("/inactive")

	public ResponseEntity<List<CustomerDto>> getInactiveCustomers() {

		return new ResponseEntity<>(customerService.getInactiveCustomers(), HttpStatus.OK);

	}
 
	@GetMapping("/phone/{phone}")

	public ResponseEntity<List<CustomerDto>> getCustomersByPhone(@PathVariable String phone) throws ResourceNotFoundException {

		List<CustomerDto> customers = customerService.getCustomersByPhone(phone);

		return new ResponseEntity<>(customers, HttpStatus.OK);

	}

	@GetMapping("/city/{city}")

	public ResponseEntity<List<CustomerDto>> getCustomersByCity(@PathVariable String city) throws ResourceNotFoundException {

		return new ResponseEntity<>(customerService.getCustomersByCityName(city), HttpStatus.OK);

	}

	@GetMapping("/country/{country}")

	public ResponseEntity<List<CustomerDto>> getCustomersByCountry(@PathVariable String country) throws ResourceNotFoundException {

		return new ResponseEntity<>(customerService.getCustomersByCountryName(country), HttpStatus.OK);

	}
 
	@PutMapping("/update/{id}/firstname")

	public ResponseEntity<CustomerDto> updateFirstName(@PathVariable Long id, @RequestBody String firstName)

			throws ResourceNotFoundException {

		return new ResponseEntity<>(customerService.updateFirstName(id, firstName), HttpStatus.OK);

	}
 
	@PutMapping("/update/{id}/lastname")

	public ResponseEntity<CustomerDto> updateLastName(@PathVariable Long id, @RequestBody String lastName)

			throws ResourceNotFoundException {

		return new ResponseEntity<>(customerService.updateLastName(id, lastName), HttpStatus.OK);

	}
 
	@PutMapping("/updateemail/{id}")

	public ResponseEntity<CustomerDto> updateEmail(@PathVariable Long id, @RequestBody String email)

			throws ResourceNotFoundException {

		return new ResponseEntity<>(customerService.updateEmail(id, email), HttpStatus.OK);

	}
 
	@PutMapping("/updatephone/{id}")

	public ResponseEntity<CustomerDto> updatePhoneNumber(@PathVariable Long id, @RequestBody String phone)

			throws ResourceNotFoundException {

		return new ResponseEntity<>(customerService.updateCustomerPhone(id, phone), HttpStatus.OK);

	}

	@PutMapping("/{id}/{addressId}")

	public ResponseEntity<CustomerDto> updateCustomerAddress(@PathVariable Long id, @PathVariable Long addressId) throws ResourceNotFoundException {

        return new ResponseEntity<>(customerService.assignAddressToCustomer(id, addressId), HttpStatus.OK);

    }

	@PutMapping("/update/{id}/store")

	public ResponseEntity<CustomerDto> updateCustomerStore(@PathVariable Long id, @RequestBody Store store) throws ResourceNotFoundException {

        return new ResponseEntity<>(customerService.assignStoreToCustomer(id, store), HttpStatus.OK);

    }
 
}

 