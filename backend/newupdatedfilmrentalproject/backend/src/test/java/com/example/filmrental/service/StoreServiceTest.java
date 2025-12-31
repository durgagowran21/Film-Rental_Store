
package com.example.filmrental.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import com.example.filmrental.dto.CustomerDto;
import com.example.filmrental.dto.ManagerSummaryDto;
import com.example.filmrental.dto.StaffDTO;
import com.example.filmrental.dto.StoreDto;
import com.example.filmrental.dto.StoreIdDTO;
import com.example.filmrental.exception.ResourceNotFoundException;
import com.example.filmrental.model.Address;
import com.example.filmrental.model.City;
import com.example.filmrental.model.Country;
import com.example.filmrental.model.Customer;
import com.example.filmrental.model.Staff;
import com.example.filmrental.model.Store;
import com.example.filmrental.repository.AddressRepository;
import com.example.filmrental.repository.CustomerRepository;
import com.example.filmrental.repository.StaffRepository;
import com.example.filmrental.repository.StoreRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

/** Unit tests for StoreService */
@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock private StoreRepository storeRepository;
    @Mock private AddressRepository addressRepository;
    @Mock private StaffRepository staffRepository;
    @Mock private CustomerRepository customerRepository;
    @Mock private ModelMapper modelMapper;

    @InjectMocks
    private StoreService storeService;

    // ------------------ getAllStores ------------------
    @Test
    void getAllStores_success() {
        Store s1 = storeWithAddr(1L, "Chennai", "India", "999");
        Store s2 = storeWithAddr(2L, "Mumbai", "India", "888");
        when(storeRepository.findAll()).thenReturn(Arrays.asList(s1, s2));

        List<Store> result = storeService.getAllStores();

        assertThat(result).hasSize(2);
        verify(storeRepository).findAll();
    }

    // ------------------ addStore ------------------
    @Test
    void addStore_success() throws Exception {
        // StoreDto is not shown; mock it to provide addressId
        StoreDto input = mock(StoreDto.class);
        when(input.getAddressID()).thenReturn(5L);

        Address addr = new Address(); addr.setAddressId(5L);
        when(addressRepository.findById(5L)).thenReturn(Optional.of(addr));

        Store saved = new Store();
        saved.setStoreId(10L);
        saved.setAddress(addr);
        when(storeRepository.save(any(Store.class))).thenReturn(saved);

        Store result = storeService.addStore(input);

        assertThat(result.getStoreId()).isEqualTo(10L);
        assertThat(result.getAddress()).isEqualTo(addr);
        verify(addressRepository).findById(5L);
        verify(storeRepository).save(any(Store.class));
    }

    @Test
    void addStore_addressNotFound_throws() {
        StoreDto input = mock(StoreDto.class);
        when(input.getAddressID()).thenReturn(99L);
        when(addressRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> storeService.addStore(input));
    }

    // ------------------ assignAddressToStore ------------------
    @Test
    void assignAddressToStore_success() throws Exception {
        Store store = storeWithAddr(7L, "Chennai", "India", "111");
        Address newAddr = address("Adyar", "Chennai", "India", "222");

        when(storeRepository.findById(7L)).thenReturn(Optional.of(store));
        when(addressRepository.findById(100L)).thenReturn(Optional.of(newAddr));
        when(storeRepository.save(store)).thenReturn(store);

        StoreDto dto = new StoreDto(); // if StoreDto has no setters used here, mapper stub returns empty DTO
        when(modelMapper.map(store, StoreDto.class)).thenReturn(dto);

        StoreDto result = storeService.assignAddressToStore(7L, 100L);

        assertThat(result).isNotNull();
        assertThat(store.getAddress()).isEqualTo(newAddr);
        verify(storeRepository).save(store);
    }

    @Test
    void assignAddressToStore_storeNotFound_throws() {
        when(storeRepository.findById(123L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> storeService.assignAddressToStore(123L, 5L));
    }

    @Test
    void assignAddressToStore_addressNotFound_throws() {
        Store store = storeWithAddr(7L, "Chennai", "India", "111");
        when(storeRepository.findById(7L)).thenReturn(Optional.of(store));
        when(addressRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> storeService.assignAddressToStore(7L, 100L));
    }

    // ------------------ getStoresByCity ------------------
    @Test
    void getStoresByCity_success() throws Exception {
        Store s1 = storeWithAddr(1L, "Chennai", "India", "999");
        Store s2 = storeWithAddr(2L, "Chennai", "India", "888");
        Store s3 = storeWithAddr(3L, "Mumbai", "India", "777");

        when(storeRepository.findAll()).thenReturn(Arrays.asList(s1, s2, s3));
        when(modelMapper.map(any(Store.class), eq(StoreDto.class)))
                .thenAnswer(inv -> new StoreDto());

        List<StoreDto> result = storeService.getStoresByCity("Chennai");

        assertThat(result).hasSize(2);
        verify(storeRepository).findAll();
    }

    @Test
    void getStoresByCity_empty_throws() {
        Store s3 = storeWithAddr(3L, "Mumbai", "India", "777");
        when(storeRepository.findAll()).thenReturn(Collections.singletonList(s3));

        assertThrows(ResourceNotFoundException.class, () -> storeService.getStoresByCity("Chennai"));
    }

    // ------------------ getStoreIdsByCountry ------------------
    @Test
    void getStoreIdsByCountry_success() throws Exception {
        Store s1 = storeWithAddr(1L, "Chennai", "India", "999");
        Store s2 = storeWithAddr(2L, "Mumbai",  "India", "888");
        Store s3 = storeWithAddr(3L, "Dallas",  "USA",   "111");

        when(storeRepository.findAll()).thenReturn(Arrays.asList(s1, s2, s3));

        List<StoreIdDTO> result = storeService.getStoreIdsByCountry("India");

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getStoreId()).isEqualTo(1L);
        assertThat(result.get(1).getStoreId()).isEqualTo(2L);
    }

    @Test
    void getStoreIdsByCountry_empty_throws() {
        Store s3 = storeWithAddr(3L, "Dallas", "USA", "111");
        when(storeRepository.findAll()).thenReturn(Collections.singletonList(s3));

        assertThrows(ResourceNotFoundException.class, () -> storeService.getStoreIdsByCountry("India"));
    }

    // ------------------ getStoreByPhone ------------------
    @Test
    void getStoreByPhone_success() throws Exception {
        String phone = "999";
        Store store = storeWithAddr(5L, "Chennai", "India", phone);

        // Address is mocked to control getStores()
        Address addr = mock(Address.class);
        when(addr.getStores()).thenReturn(Collections.singletonList(store));
        when(addressRepository.findByPhone(phone)).thenReturn(addr);

        StoreDto dto = new StoreDto();
        when(modelMapper.map(store, StoreDto.class)).thenReturn(dto);

        StoreDto result = storeService.getStoreByPhone(phone);

        assertThat(result).isNotNull();
        verify(addressRepository).findByPhone(phone);
    }

    @Test
    void getStoreByPhone_addressNotFound_throws() {
        when(addressRepository.findByPhone("000")).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> storeService.getStoreByPhone("000"));
    }

    @Test
    void getStoreByPhone_noStoreForAddress_throws() {
        Address addr = mock(Address.class);
        when(addr.getStores()).thenReturn(Collections.emptyList());
        when(addressRepository.findByPhone("999")).thenReturn(addr);

        assertThrows(ResourceNotFoundException.class, () -> storeService.getStoreByPhone("999"));
    }

    // ------------------ updatePhoneNumber ------------------
    @Test
    void updatePhoneNumber_success() throws Exception {
        Store store = storeWithAddr(5L, "Chennai", "India", "111");
        Address address = store.getAddress();

        when(storeRepository.findById(5L)).thenReturn(Optional.of(store));
        when(addressRepository.save(address)).thenReturn(address);

        String msg = storeService.updatePhoneNumber(5L, "222");

        assertThat(address.getPhone()).isEqualTo("222");
        assertThat(msg).contains("Updated Successfully");
        verify(addressRepository).save(address);
    }

    @Test
    void updatePhoneNumber_noAddress_throws() {
        Store store = new Store();
        store.setStoreId(5L);
        store.setAddress(null);

        when(storeRepository.findById(5L)).thenReturn(Optional.of(store));

        assertThrows(ResourceNotFoundException.class, () -> storeService.updatePhoneNumber(5L, "222"));
    }

    @Test
    void updatePhoneNumber_storeNotFound_throws() {
        when(storeRepository.findById(404L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> storeService.updatePhoneNumber(404L, "222"));
    }

    // ------------------ getStaffByStoreId ------------------
    @Test
    void getStaffByStoreId_success() throws Exception {
        Staff a = staff(1L, "Alice", "Doe", "a@b.com");
        Staff b = staff(2L, "Bob", "Doe", "b@b.com");
        Store store = new Store();
        store.setStoreId(9L);
        store.setStaffList(Arrays.asList(a, b));
        when(storeRepository.findById(9L)).thenReturn(Optional.of(store));

        List<Staff> result = storeService.getStaffByStoreId(9L);

        assertThat(result).hasSize(2);
        verify(storeRepository).findById(9L);
    }

    @Test
    void getStaffByStoreId_storeNotFound_throws() {
        when(storeRepository.findById(9L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> storeService.getStaffByStoreId(9L));
    }

    // ------------------ getCustomersByStoreId ------------------
    @Test
    void getCustomersByStoreId_success() throws Exception {
        Store store = new Store(); store.setStoreId(3L);
        when(storeRepository.findById(3L)).thenReturn(Optional.of(store));

        Customer c1 = new Customer(); c1.setCustomerId(11L);
        Customer c2 = new Customer(); c2.setCustomerId(12L);
        when(customerRepository.findByStore_StoreId(3L)).thenReturn(Arrays.asList(c1, c2));

        when(modelMapper.map(any(Customer.class), eq(CustomerDto.class)))
                .thenAnswer(inv -> new CustomerDto());

        List<CustomerDto> result = storeService.getCustomersByStoreId(3L);

        assertThat(result).hasSize(2);
        verify(customerRepository).findByStore_StoreId(3L);
    }

    @Test
    void getCustomersByStoreId_storeNotFound_throws() {
        when(storeRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> storeService.getCustomersByStoreId(3L));
    }

    @Test
    void getCustomersByStoreId_noCustomers_throws() {
        Store store = new Store(); store.setStoreId(3L);
        when(storeRepository.findById(3L)).thenReturn(Optional.of(store));
        when(customerRepository.findByStore_StoreId(3L)).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> storeService.getCustomersByStoreId(3L));
    }
//
//    // ------------------ assignManagerToStore ------------------
//    @Test
//    void assignManagerToStore_success() throws Exception {
//        Store store = storeWithAddr(5L, "Chennai", "India", "111");
//        Staff mgr = staff(77L, "Manager", "One", "m@b.com");
//
//        when(storeRepository.findById(5L)).thenReturn(Optional.of(store));
//        when(staffRepository.findById(77L)).thenReturn(Optional.of(mgr));
//        when(storeRepository.save(store)).thenReturn(store);
//
//        StoreDto dto = new StoreDto();
//        when(modelMapper.map(store, StoreDto.class)).thenReturn(dto);
//
//        StoreDto result = storeService.assignManagerToStore(5L, 77L);
//
//        assertThat(result).isNotNull();
//        assertThat(store.getManager()).isEqualTo(mgr);
//        verify(storeRepository).save(store);
//    }
//
//    @Test
//    void assignManagerToStore_storeNotFound_throws() {
//        when(storeRepository.findById(5L)).thenReturn(Optional.empty());
//        assertThrows(ResourceNotFoundException.class, () -> storeService.assignManagerToStore(5L, 77L));
//    }
//
//    @Test
//    void assignManagerToStore_managerNotFound_throws() {
//        Store store = storeWithAddr(5L, "Chennai", "India", "111");
//        when(storeRepository.findById(5L)).thenReturn(Optional.of(store));
//        when(staffRepository.findById(77L)).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class, () -> storeService.assignManagerToStore(5L, 77L));
//    }

//    // ------------------ getManagerOfStore ------------------
//    @Test
//    void getManagerOfStore_success() throws Exception {
//        Store store = storeWithAddr(5L, "Chennai", "India", "111");
//        Staff mgr = staff(77L, "Manager", "One", "m@b.com");
//        store.setManager(mgr);
//
//        when(storeRepository.findById(5L)).thenReturn(Optional.of(store));
//
//        StaffDTO dto = new StaffDTO();
//        when(modelMapper.map(mgr, StaffDTO.class)).thenReturn(dto);
//
//        StaffDTO result = storeService.getManagerOfStore(5L);
//
//        assertThat(result).isNotNull();
//    }

//    @Test
//    void getManagerOfStore_storeNotFound_throws() {
//        when(storeRepository.findById(5L)).thenReturn(Optional.empty());
//        assertThrows(ResourceNotFoundException.class, () -> storeService.getManagerOfStore(5L));
//    }
//
//    @Test
//    void getManagerOfStore_noManager_throws() {
//        Store store = storeWithAddr(5L, "Chennai", "India", "111");
//        store.setManager(null);
//        when(storeRepository.findById(5L)).thenReturn(Optional.of(store));
//
//        assertThrows(ResourceNotFoundException.class, () -> storeService.getManagerOfStore(5L));
//    }

//    // ------------------ getAllStoreManagers ------------------
//    @Test
//    void getAllStoreManagers_success_onlyStoresWithManagersIncluded() {
//        Store s1 = storeWithAddr(1L, "Chennai", "India", "999");
//        Store s2 = storeWithAddr(2L, "Mumbai", "India", "888");
//        Store s3 = storeWithAddr(3L, "Dallas", "USA", "111");
//
//        Staff m1 = staff(10L, "Alice", "Mgr", "alice@films.com");
//        Staff m2 = staff(11L, "Bob",   "Mgr", "bob@films.com");
//        s1.setManager(m1); // included
//        s2.setManager(m2); // included
//        s3.setManager(null); // filtered out
//
//        when(storeRepository.findAll()).thenReturn(Arrays.asList(s1, s2, s3));
//
//        List<ManagerSummaryDto> result = storeService.getAllStoreManagers();
//
//        assertThat(result).hasSize(2);
//        // Optional spot checks
//        assertThat(result.get(0).getStoreId()).isEqualTo(1L);
//        assertThat(result.get(1).getStoreId()).isEqualTo(2L);
//    }

    // =================== helpers ===================
    private static Store storeWithAddr(Long storeId, String cityName, String countryName, String phone) {
        Address addr = address("Line1", cityName, countryName, phone);
        Store store = new Store();
        store.setStoreId(storeId);
        store.setAddress(addr);
        store.setLastUpdate(LocalDateTime.now());
        return store;
    }

    private static Address address(String line1, String cityName, String countryName, String phone) {
        Country country = new Country(); country.setCountry(countryName);
        City city = new City(); city.setCityName(cityName); city.setCountry(country);
        Address addr = new Address();
        addr.setAddress(line1);
        addr.setPhone(phone);
        addr.setCity(city);
        addr.setLastUpdate(LocalDateTime.now());
        return addr;
    }

    private static Staff staff(Long id, String first, String last, String email) {
        Staff s = new Staff();
        s.setStaffId(id);
        s.setFirstName(first);
        s.setLastName(last);
        s.setEmail(email);
        s.setLastupdate(LocalDateTime.now());
        return s;
    }
}

