
package com.example.filmrental.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.filmrental.dto.CustomerDto;
import com.example.filmrental.exception.ResourceNotFoundException;
import com.example.filmrental.model.Address;
import com.example.filmrental.model.Customer;
import com.example.filmrental.model.Store;
import com.example.filmrental.repository.AddressRepository;
import com.example.filmrental.repository.CustomerRepository;
import com.example.filmrental.repository.StoreRepository;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private StoreRepository storeRepository;

    // Use real ModelMapper to mirror service behavior
    private final ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    private CustomerService customerService = new CustomerService();

    // Helper to configure the service's internal modelMapper if needed
    // Only necessary if CustomerService doesn't use Spring to inject it
    {
        // The service constructs its own ModelMapper. If you want to
        // override, you can reflectively set it or rely on its internal instance.
        // Here we rely on service's own instance as per your code. [1](https://capgemini-my.sharepoint.com/personal/muthumareeswari_p_capgemini_com/Documents/Microsoft%20Copilot%20Chat%20Files/cus%20ser%201.txt)
    }

    @Test
    void getCustomerById_success() throws Exception {
        Customer c = new Customer();
        c.setCustomerId(1L);
        c.setFirstName("JOHN");
        c.setLastName("DOE");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(c));

        CustomerDto dto = customerService.getCustomerById(1L);

        assertThat(dto).isNotNull();
        assertThat(dto.getCustomerId()).isEqualTo(1L);
        verify(customerRepository).findById(1L);
    }

    @Test
    void getCustomerById_notFound_throws() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomerById(99L));
    }

    @Test
    void getAllCustomers_success() {
        Customer c1 = new Customer(); c1.setCustomerId(1L); c1.setFirstName("A"); c1.setLastName("B");
        Customer c2 = new Customer(); c2.setCustomerId(2L); c2.setFirstName("C"); c2.setLastName("D");
        when(customerRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<CustomerDto> result = customerService.getAllCustomers();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCustomerId()).isEqualTo(1L);
        assertThat(result.get(1).getCustomerId()).isEqualTo(2L);
        verify(customerRepository).findAll();
    }

    @Test
    void createCustomer_success() {
        CustomerDto input = new CustomerDto();
        input.setCustomerId(10L);
        input.setFirstName("X");
        input.setLastName("Y");

        Customer toSave = new Customer();
        toSave.setCustomerId(10L);
        toSave.setFirstName("X");
        toSave.setLastName("Y");

        Customer saved = new Customer();
        saved.setCustomerId(10L);
        saved.setFirstName("X");
        saved.setLastName("Y");

        when(customerRepository.save(org.mockito.ArgumentMatchers.any(Customer.class))).thenReturn(saved);

        CustomerDto result = customerService.createCustomer(input);

        assertThat(result.getCustomerId()).isEqualTo(10L);
        assertThat(result.getFirstName()).isEqualTo("X");
        verify(customerRepository).save(org.mockito.ArgumentMatchers.any(Customer.class));
    }

    @Test
    void updateCustomer_success() throws Exception {
        Customer existing = new Customer();
        existing.setCustomerId(5L);
        existing.setFirstName("OLD");
        existing.setLastName("NAME");

        CustomerDto update = new CustomerDto();
        update.setFirstName("NEW");
        update.setLastName("NAME");

        Customer updated = new Customer();
        updated.setCustomerId(5L);
        updated.setFirstName("NEW");
        updated.setLastName("NAME");

        when(customerRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(customerRepository.save(existing)).thenReturn(updated);

        CustomerDto result = customerService.updateCustomer(5L, update);

        assertThat(result.getCustomerId()).isEqualTo(5L);
        assertThat(result.getFirstName()).isEqualTo("NEW");
        verify(customerRepository).findById(5L);
        verify(customerRepository).save(existing);
    }

    @Test
    void updateCustomer_notFound_throws() {
        CustomerDto update = new CustomerDto();
        update.setFirstName("NEW");
        when(customerRepository.findById(404L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> customerService.updateCustomer(404L, update));
    }

    @Test
    void assignAddressToCustomer_success() throws Exception {
        Customer cust = new Customer(); cust.setCustomerId(1L);
        Address addr = new Address(); addr.setAddressId(9L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(cust));
        when(addressRepository.findById(9L)).thenReturn(Optional.of(addr));
        when(customerRepository.save(cust)).thenReturn(cust);

        CustomerDto result = customerService.assignAddressToCustomer(1L, 9L);

        assertThat(result.getCustomerId()).isEqualTo(1L);
        verify(customerRepository).save(cust);
    }

    @Test
    void assignAddressToCustomer_customerNotFound_throws() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> customerService.assignAddressToCustomer(1L, 9L));
    }

    @Test
    void assignAddressToCustomer_addressNotFound_throws() {
        Customer cust = new Customer(); cust.setCustomerId(1L);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(cust));
        when(addressRepository.findById(9L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> customerService.assignAddressToCustomer(1L, 9L));
    }

    @Test
    void assignNewAddressToCustomer_success() throws Exception {
        Customer cust = new Customer(); cust.setCustomerId(2L);
        Address newAddr = new Address(); newAddr.setAddressId(11L);
        Address savedAddr = new Address(); savedAddr.setAddressId(11L);

        when(customerRepository.findById(2L)).thenReturn(Optional.of(cust));
        when(addressRepository.save(newAddr)).thenReturn(savedAddr);
        when(customerRepository.save(cust)).thenReturn(cust);

        CustomerDto result = customerService.assignNewAddressToCustomer(2L, newAddr);

        assertThat(result.getCustomerId()).isEqualTo(2L);
        verify(addressRepository).save(newAddr);
        verify(customerRepository).save(cust);
    }

    @Test
    void getCustomersByLastName_success() throws Exception {
        Customer c = new Customer(); c.setCustomerId(1L); c.setLastName("DOE");
        when(customerRepository.findByLastName("DOE")).thenReturn(Arrays.asList(c));

        List<CustomerDto> result = customerService.getCustomersByLastName("Doe");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getLastName()).isEqualTo("DOE");
    }

    @Test
    void getCustomersByLastName_empty_throws() {
        when(customerRepository.findByLastName("DOE")).thenReturn(Collections.emptyList());
        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomersByLastName("Doe"));
    }

    @Test
    void getCustomersByFirstName_success() throws Exception {
        Customer c = new Customer(); c.setCustomerId(1L); c.setFirstName("JOHN");
        when(customerRepository.findByFirstName("JOHN")).thenReturn(Arrays.asList(c));

        List<CustomerDto> result = customerService.getCustomersByFirstName("John");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("JOHN");
    }

    @Test
    void getCustomersByFirstName_empty_throws() {
        when(customerRepository.findByFirstName("JOHN")).thenReturn(Collections.emptyList());
        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomersByFirstName("John"));
    }

    @Test
    void getCustomersByEmail_success() throws Exception {
        Customer c = new Customer(); c.setCustomerId(1L); c.setEmail("a@b.com");
        when(customerRepository.findByEmail("a@b.com")).thenReturn(Arrays.asList(c));

        List<CustomerDto> result = customerService.getCustomersByEmail("a@b.com");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("a@b.com");
    }

    @Test
    void getCustomersByEmail_empty_throws() {
        when(customerRepository.findByEmail("none@b.com")).thenReturn(Collections.emptyList());
        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomersByEmail("none@b.com"));
    }

    @Test
    void getCustomersByCityName_success() throws Exception {
        Customer c = new Customer(); c.setCustomerId(1L);
        when(customerRepository.findByAddressCityCityName("Chennai")).thenReturn(Arrays.asList(c));

        List<CustomerDto> result = customerService.getCustomersByCityName("Chennai");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCustomerId()).isEqualTo(1L);
    }

    @Test
    void getCustomersByCityName_empty_throws() {
        when(customerRepository.findByAddressCityCityName("Chennai")).thenReturn(Collections.emptyList());
        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomersByCityName("Chennai"));
    }

    @Test
    void getCustomersByCountryName_success() throws Exception {
        Customer c = new Customer(); c.setCustomerId(1L);
        when(customerRepository.findByAddressCityCountryCountry("India")).thenReturn(Arrays.asList(c));

        List<CustomerDto> result = customerService.getCustomersByCountryName("India");

        assertThat(result).hasSize(1);
    }

    @Test
    void getCustomersByCountryName_empty_throws() {
        when(customerRepository.findByAddressCityCountryCountry("India")).thenReturn(Collections.emptyList());
        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomersByCountryName("India"));
    }

    @Test
    void getActiveCustomers_success() {
        Customer c = new Customer(); c.setCustomerId(1L);
        when(customerRepository.findByActive(true)).thenReturn(Arrays.asList(c));

        List<CustomerDto> result = customerService.getActiveCustomers();

        assertThat(result).hasSize(1);
    }

    @Test
    void getInactiveCustomers_success() {
        Customer c = new Customer(); c.setCustomerId(2L);
        when(customerRepository.findByActive(false)).thenReturn(Arrays.asList(c));

        List<CustomerDto> result = customerService.getInactiveCustomers();

        assertThat(result).hasSize(1);
    }

    @Test
    void updateFirstName_success() throws Exception {
        Customer existing = new Customer(); existing.setCustomerId(3L); existing.setFirstName("OLD");
        Customer saved = new Customer(); saved.setCustomerId(3L); saved.setFirstName("NEW");

        when(customerRepository.findById(3L)).thenReturn(Optional.of(existing));
        when(customerRepository.save(existing)).thenReturn(saved);

        CustomerDto result = customerService.updateFirstName(3L, "NEW");

        assertThat(result.getFirstName()).isEqualTo("NEW");
    }

    @Test
    void updateFirstName_notFound_throws() {
        when(customerRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> customerService.updateFirstName(3L, "NEW"));
    }

    @Test
    void updateLastName_success() throws Exception {
        Customer existing = new Customer(); existing.setCustomerId(4L); existing.setLastName("OLD");
        Customer saved = new Customer(); saved.setCustomerId(4L); saved.setLastName("NEW");

        when(customerRepository.findById(4L)).thenReturn(Optional.of(existing));
        when(customerRepository.save(existing)).thenReturn(saved);

        CustomerDto result = customerService.updateLastName(4L, "NEW");

        assertThat(result.getLastName()).isEqualTo("NEW");
    }

    @Test
    void updateLastName_notFound_throws() {
        when(customerRepository.findById(4L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> customerService.updateLastName(4L, "NEW"));
    }

    @Test
    void getCustomersByPhone_success() throws Exception {
        Address a = new Address(); a.setPhone("999");
        Customer c = new Customer(); c.setCustomerId(1L); c.setAddress(a);
        when(customerRepository.findByAddressPhone("999")).thenReturn(Arrays.asList(c));

        List<CustomerDto> result = customerService.getCustomersByPhone("999");

        assertThat(result).hasSize(1);
    }

    @Test
    void getCustomersByPhone_empty_throws() {
        when(customerRepository.findByAddressPhone("000")).thenReturn(Collections.emptyList());
        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomersByPhone("000"));
    }

    @Test
    void updateEmail_success() throws Exception {
        Customer existing = new Customer(); existing.setCustomerId(7L);
        Customer saved = new Customer(); saved.setCustomerId(7L); saved.setEmail("x@y.com");

        when(customerRepository.findById(7L)).thenReturn(Optional.of(existing));
        when(customerRepository.save(existing)).thenReturn(saved);

        CustomerDto result = customerService.updateEmail(7L, "x@y.com");

        assertThat(result.getEmail()).isEqualTo("x@y.com");
    }

    @Test
    void updateEmail_notFound_throws() {
        when(customerRepository.findById(7L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> customerService.updateEmail(7L, "x@y.com"));
    }

    @Test
    void updateCustomerPhone_notFound_throws() {
        when(customerRepository.findById(8L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> customerService.updateCustomerPhone(8L, "222"));
    }

    @Test
    void assignStoreToCustomer_success() throws Exception {
        Customer cust = new Customer(); cust.setCustomerId(12L);
        Store store = new Store(); store.setStoreId(3L);

        when(customerRepository.findById(12L)).thenReturn(Optional.of(cust));
        when(storeRepository.findById(3L)).thenReturn(Optional.of(store));
        when(customerRepository.save(cust)).thenReturn(cust);

        CustomerDto result = customerService.assignStoreToCustomer(12L, store);

        assertThat(result.getCustomerId()).isEqualTo(12L);
        verify(customerRepository).save(cust);
    }

    @Test
    void assignStoreToCustomer_customerNotFound_throws() {
        Store store = new Store(); store.setStoreId(3L);
        when(customerRepository.findById(12L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> customerService.assignStoreToCustomer(12L, store));
    }

    @Test
    void assignStoreToCustomer_storeNotFound_throws() {
        Customer cust = new Customer(); cust.setCustomerId(12L);
        Store store = new Store(); store.setStoreId(3L);
        when(customerRepository.findById(12L)).thenReturn(Optional.of(cust));
        when(storeRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> customerService.assignStoreToCustomer(12L, store));
    }
}
