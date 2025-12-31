package com.example.filmrental.service;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.filmrental.dto.CustomerDto;
import com.example.filmrental.exception.ResourceNotFoundException;
import com.example.filmrental.model.Address;
import com.example.filmrental.model.Customer;
import com.example.filmrental.model.Store;
import com.example.filmrental.repository.AddressRepository;
import com.example.filmrental.repository.CustomerRepository;
import com.example.filmrental.repository.StoreRepository;
 

 
@Service

public class CustomerService { 
	@Autowired
    private CustomerRepository customerRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    private StoreRepository storeRepository;

//    @Autowired

//    ModelMapper modelMapper;

    ModelMapper modelMapper = new ModelMapper();
 
   

    public CustomerDto getCustomerById(Long id) throws ResourceNotFoundException {

        Customer customer = customerRepository.findById(id)

            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        return modelMapper.map(customer, CustomerDto.class);

    }
 
    

    public List<CustomerDto> getAllCustomers() {

        return customerRepository.findAll().stream()

            .map(customer -> modelMapper.map(customer, CustomerDto.class))

            .collect(Collectors.toList());

    }
 
   

    public CustomerDto createCustomer(CustomerDto customerDTO) {

        Customer customer = modelMapper.map(customerDTO, Customer.class);

        Customer savedCustomer = customerRepository.save(customer);

        return modelMapper.map(savedCustomer, CustomerDto.class);

    }
 
    

    public CustomerDto updateCustomer(Long id, CustomerDto customerDTO) throws ResourceNotFoundException {

        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        modelMapper.map(customerDTO, customer);

        Customer updatedCustomer = customerRepository.save(customer);

        return modelMapper.map(updatedCustomer, CustomerDto.class);

    }
 


    public CustomerDto assignAddressToCustomer(Long customerId, Long addressId) throws ResourceNotFoundException {

        Customer customer = customerRepository.findById(customerId)

            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        Address address = addressRepository.findById(addressId)

            .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));

        customer.setAddress(address);

        Customer updatedCustomer = customerRepository.save(customer);

        return modelMapper.map(updatedCustomer, CustomerDto.class);

    }

 

    public CustomerDto assignNewAddressToCustomer(Long customerId, Address address) throws ResourceNotFoundException {

        Customer customer = customerRepository.findById(customerId)

                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
 
        Address savedAddress = addressRepository.save(address);

        customer.setAddress(savedAddress);

        Customer updatedCustomer = customerRepository.save(customer);

        return modelMapper.map(updatedCustomer, CustomerDto.class);

    }
 

    public List<CustomerDto> getCustomersByLastName(String ln) throws ResourceNotFoundException {

    	String lastName = ln.toUpperCase();

        List<Customer> customers = customerRepository.findByLastName(lastName);

        if (customers.isEmpty()) {

            throw new ResourceNotFoundException("No customers found with last name: " + lastName);

        }

        return customers.stream()

                .map(customer -> modelMapper.map(customer, CustomerDto.class))

                .collect(Collectors.toList());

    }
 

    public List<CustomerDto> getCustomersByFirstName(String fn) throws ResourceNotFoundException {

    	String firstName = fn.toUpperCase();

        List<Customer> customers = customerRepository.findByFirstName(firstName);

        if (customers.isEmpty()) {

            throw new ResourceNotFoundException("No customers found with first name: " + firstName);

        }

        return customers.stream()

                .map(customer -> modelMapper.map(customer, CustomerDto.class))

                .collect(Collectors.toList());

    }
 

    public List<CustomerDto> getCustomersByEmail(String email) throws ResourceNotFoundException {

        List<Customer> customers = customerRepository.findByEmail(email);

        if (customers.isEmpty()) {

            throw new ResourceNotFoundException("No customer found with email: " + email);

        }

        return customers.stream()

                .map(customer -> modelMapper.map(customer, CustomerDto.class))

                .collect(Collectors.toList());

    }
 
    

    public List<CustomerDto> getCustomersByCityName(String cityName) throws ResourceNotFoundException {

        List<Customer> customers = customerRepository.findByAddressCityCityName(cityName);

        if (customers.isEmpty()) {

            throw new ResourceNotFoundException("No customers found in " + cityName);

        }

        return customers.stream()

                .map(customer -> modelMapper.map(customer, CustomerDto.class))

                .collect(Collectors.toList());

    }
 


    public List<CustomerDto> getCustomersByCountryName(String countryName) throws ResourceNotFoundException {

        List<Customer> customers = customerRepository.findByAddressCityCountryCountry(countryName);

        if (customers.isEmpty()) {

            throw new ResourceNotFoundException("No customers found in " + countryName);

        }

        return customers.stream()

                .map(customer -> modelMapper.map(customer, CustomerDto.class))

                .collect(Collectors.toList());

    }
 
 

    public List<CustomerDto> getActiveCustomers() {

        return customerRepository.findByActive(true).stream()

            .map(customer -> modelMapper.map(customer, CustomerDto.class))

            .collect(Collectors.toList());

    }


    public List<CustomerDto> getInactiveCustomers() {

        return customerRepository.findByActive(false).stream()

            .map(customer -> modelMapper.map(customer, CustomerDto.class))

            .collect(Collectors.toList());

    }


    public CustomerDto updateFirstName(Long id, String firstName) throws ResourceNotFoundException {

        Customer customer = customerRepository.findById(id)

            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        customer.setFirstName(firstName);

        Customer updatedCustomer = customerRepository.save(customer);

        return modelMapper.map(updatedCustomer, CustomerDto.class);

    }
 

    public CustomerDto updateLastName(Long id, String lastName) throws ResourceNotFoundException {

        Customer customer = customerRepository.findById(id)

            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        customer.setLastName(lastName);

        Customer updatedCustomer = customerRepository.save(customer);

        return modelMapper.map(updatedCustomer, CustomerDto.class);

    }


    public List<CustomerDto> getCustomersByPhone(String phone) throws ResourceNotFoundException {

        List<Customer> customers = customerRepository.findByAddressPhone(phone);

        if (customers.isEmpty()) {

            throw new ResourceNotFoundException("No customers found with phone " + phone);

        }

        return customers.stream().map(customer -> {

            CustomerDto customerDTO = new CustomerDto();

            modelMapper.map(customer, customerDTO);

            return customerDTO;

        }).collect(Collectors.toList());

    }

    public CustomerDto updateEmail(Long id, String email) throws ResourceNotFoundException {

        Customer customer = customerRepository.findById(id)

            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        customer.setEmail(email);

        Customer updatedCustomer = customerRepository.save(customer);

        return modelMapper.map(updatedCustomer, CustomerDto.class);

    }

    public CustomerDto updateCustomerPhone(Long customerId, String phone) throws ResourceNotFoundException {

        Customer customer = customerRepository.findById(customerId)

                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        customer.getAddress().setPhone(phone);

        Customer updatedCustomer = customerRepository.save(customer);

        return modelMapper.map(updatedCustomer, CustomerDto.class);

    }
 
    

    public CustomerDto assignStoreToCustomer(Long customerId, Store store) throws ResourceNotFoundException {

        // Fetch the customer by ID

        Customer customer = customerRepository.findById(customerId)

            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        Store existingStore = storeRepository.findById(store.getStoreId())

            .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + store.getStoreId()));

        customer.setStore(existingStore);

        Customer updatedCustomer = customerRepository.save(customer);

 
        return modelMapper.map(updatedCustomer, CustomerDto.class);

    }
 
}

 