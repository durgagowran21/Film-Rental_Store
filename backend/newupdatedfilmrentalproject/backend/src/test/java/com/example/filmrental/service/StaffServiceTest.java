
package com.example.filmrental.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.example.filmrental.dto.StaffDTO;
import com.example.filmrental.exception.BadRequestException;
import com.example.filmrental.exception.ResourceNotFoundException;
import com.example.filmrental.model.Address;
import com.example.filmrental.model.City;
import com.example.filmrental.model.Country;
import com.example.filmrental.model.Staff;
import com.example.filmrental.model.Store;
import com.example.filmrental.repository.AddressRepository;
import com.example.filmrental.repository.StaffRepository;
import com.example.filmrental.repository.StoreRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class StaffServiceTest {

    @Mock private StaffRepository staffRepo;
    @Mock private StoreRepository storeRepo;
    @Mock private AddressRepository addressRepo;
    @Mock private ModelMapper modelMapper;

    @InjectMocks
    private StaffService staffService;

    // ---------------- ModelMapper stubs ----------------
    private void stubModelMapperStaffToDTO() {
        when(modelMapper.map(any(Staff.class), eq(StaffDTO.class)))
            .thenAnswer(inv -> new StaffDTO());
    }
    private void stubModelMapperDTOToStaff() {
        when(modelMapper.map(any(StaffDTO.class), eq(Staff.class)))
            .thenAnswer(inv -> new Staff());
    }

    // ---------------- findStaffByLastName ----------------
    @Test
    void findStaffByLastName_success() throws Exception {
        Staff s1 = staff(1L, "Alice", "Doe", "alice@films.com");
        Staff s2 = staff(2L, "Bob", "Doe", "bob@films.com");
        when(staffRepo.findByLastName("Doe")).thenReturn(Arrays.asList(s1, s2));
        stubModelMapperStaffToDTO();

        List<StaffDTO> result = staffService.findStaffByLastName("Doe");

        assertThat(result).hasSize(2);
        verify(staffRepo).findByLastName("Doe");
    }

    @Test
    void findStaffByLastName_empty_throws() {
        when(staffRepo.findByLastName("NoOne")).thenReturn(Collections.emptyList());
        assertThrows(ResourceNotFoundException.class, () -> staffService.findStaffByLastName("NoOne"));
    }

    // ---------------- findStaffByFirstName ----------------
    @Test
    void findStaffByFirstName_success() throws Exception {
        Staff s1 = staff(3L, "Alice", "Y", "alice@films.com");
        when(staffRepo.findByFirstName("Alice")).thenReturn(Arrays.asList(s1));
        stubModelMapperStaffToDTO();

        List<StaffDTO> result = staffService.findStaffByFirstName("Alice");

        assertThat(result).hasSize(1);
        verify(staffRepo).findByFirstName("Alice");
    }

    @Test
    void findStaffByFirstName_empty_throws() {
        when(staffRepo.findByFirstName("Nobody")).thenReturn(Collections.emptyList());
        assertThrows(ResourceNotFoundException.class, () -> staffService.findStaffByFirstName("Nobody"));
    }

    // ---------------- findStaffByEmail ----------------
    @Test
    void findStaffByEmail_success() throws Exception {
        Staff s1 = staff(4L, "A", "B", "x@y.com");
        when(staffRepo.findOneByEmail("x@y.com")).thenReturn(Arrays.asList(s1));
        stubModelMapperStaffToDTO();

        List<StaffDTO> result = staffService.findStaffByEmail("x@y.com");

        assertThat(result).hasSize(1);
        verify(staffRepo).findOneByEmail("x@y.com");
    }

    @Test
    void findStaffByEmail_empty_throws() {
        when(staffRepo.findOneByEmail("none@y.com")).thenReturn(Collections.emptyList());
        assertThrows(ResourceNotFoundException.class, () -> staffService.findStaffByEmail("none@y.com"));
    }

    // ---------------- addStaff ----------------
    @Test
    void addStaff_success() {
        StaffDTO input = new StaffDTO();
        stubModelMapperDTOToStaff();
        Staff saved = staff(100L, "Fn", "Ln", "e@films.com");
        when(staffRepo.save(any(Staff.class))).thenReturn(saved);
        stubModelMapperStaffToDTO();

        StaffDTO result = staffService.addStaff(input);

        assertThat(result).isNotNull();
        verify(staffRepo).save(any(Staff.class));
    }

    // ---------------- GetAllStaffBYCity ----------------
    @Test
    void GetAllStaffBYCity_success() throws Exception {
        Staff s1 = staffWithAddressCity(1L, "Alice", "Y", "a@b.com", "Chennai");
        Staff s2 = staffWithAddressCity(2L, "Bob", "Y", "b@b.com", "Chennai");
        when(staffRepo.findOneByCity("Chennai")).thenReturn(Arrays.asList(s1, s2));
        stubModelMapperStaffToDTO();

        List<StaffDTO> result = staffService.GetAllStaffBYCity("Chennai");

        assertThat(result).hasSize(2);
        verify(staffRepo).findOneByCity("Chennai");
    }

    @Test
    void GetAllStaffBYCity_empty_throws() {
        when(staffRepo.findOneByCity("NoCity")).thenReturn(Collections.emptyList());
        assertThrows(ResourceNotFoundException.class, () -> staffService.GetAllStaffBYCity("NoCity"));
    }

    // ---------------- GetAllStaffBYCountry ----------------
    @Test
    void GetAllStaffBYCountry_success() throws Exception {
        Staff s1 = staffWithAddressCountry(1L, "Alice", "Y", "a@b.com", "India");
        when(staffRepo.findOneByCountry("India")).thenReturn(Arrays.asList(s1));
        stubModelMapperStaffToDTO();

        List<StaffDTO> result = staffService.GetAllStaffBYCountry("India");

        assertThat(result).hasSize(1);
        verify(staffRepo).findOneByCountry("India");
    }

    @Test
    void GetAllStaffBYCountry_empty_throws() {
        when(staffRepo.findOneByCountry("NoCountry")).thenReturn(Collections.emptyList());
        assertThrows(ResourceNotFoundException.class, () -> staffService.GetAllStaffBYCountry("NoCountry"));
    }

    // ---------------- GetAllStaffBYPhone ----------------
    @Test
    void GetAllStaffBYPhone_success() throws Exception {
        Staff s1 = staffWithAddressPhone(1L, "Alice", "Y", "a@b.com", "999");
        when(staffRepo.findOneByPhone("999")).thenReturn(Arrays.asList(s1));
        stubModelMapperStaffToDTO();

        List<StaffDTO> result = staffService.GetAllStaffBYPhone("999");

        assertThat(result).hasSize(1);
        verify(staffRepo).findOneByPhone("999");
    }

    @Test
    void GetAllStaffBYPhone_empty_throws() {
        when(staffRepo.findOneByPhone("000")).thenReturn(Collections.emptyList());
        assertThrows(ResourceNotFoundException.class, () -> staffService.GetAllStaffBYPhone("000"));
    }

    // ---------------- updateFirstName ----------------
    @Test
    void updateFirstName_success() throws Exception {
        Staff existing = staff(10L, "Old", "Last", "e@b.com");
        Staff saved = staff(10L, "New", "Last", "e@b.com");

        when(staffRepo.findById(10L)).thenReturn(Optional.of(existing));
        when(staffRepo.save(existing)).thenReturn(saved);
        stubModelMapperStaffToDTO();

        StaffDTO dto = staffService.updateFirstName(10L, "New");

        assertThat(dto).isNotNull();
        ArgumentCaptor<Staff> captor = ArgumentCaptor.forClass(Staff.class);
        verify(staffRepo).save(captor.capture());
        assertThat(captor.getValue().getFirstName()).isEqualTo("New");
    }

    @Test
    void updateFirstName_notFound_throws() {
        when(staffRepo.findById(404L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> staffService.updateFirstName(404L, "X"));
    }

    // ---------------- updateLastName ----------------
    @Test
    void updateLastName_success() throws Exception {
        Staff existing = staff(11L, "First", "Old", "e@b.com");
        Staff saved = staff(11L, "First", "New", "e@b.com");

        when(staffRepo.findById(11L)).thenReturn(Optional.of(existing));
        when(staffRepo.save(existing)).thenReturn(saved);
        stubModelMapperStaffToDTO();

        StaffDTO dto = staffService.updateLastName(11L, "New");

        assertThat(dto).isNotNull();
        ArgumentCaptor<Staff> captor = ArgumentCaptor.forClass(Staff.class);
        verify(staffRepo).save(captor.capture());
        assertThat(captor.getValue().getLastName()).isEqualTo("New");
    }

    @Test
    void updateLastName_notFound_throws() {
        when(staffRepo.findById(404L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> staffService.updateLastName(404L, "X"));
    }

    // ---------------- updateEmail ----------------
    @Test
    void updateEmail_success() throws Exception {
        Staff existing = staff(12L, "First", "Last", "old@b.com");
        Staff saved = staff(12L, "First", "Last", "new@b.com");

        when(staffRepo.findById(12L)).thenReturn(Optional.of(existing));
        when(staffRepo.save(existing)).thenReturn(saved);
        stubModelMapperStaffToDTO();

        StaffDTO dto = staffService.updateEmail(12L, "new@b.com");

        assertThat(dto).isNotNull();
        ArgumentCaptor<Staff> captor = ArgumentCaptor.forClass(Staff.class);
        verify(staffRepo).save(captor.capture());
        assertThat(captor.getValue().getEmail()).isEqualTo("new@b.com");
    }

    @Test
    void updateEmail_notFound_throws() {
        when(staffRepo.findById(404L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> staffService.updateEmail(404L, "x@y.com"));
    }

    // ---------------- updateStore ----------------
    @Test
    void updateStore_success() throws Exception {
        Staff staff = staff(20L, "A", "B", "a@b.com");
        Store store = new Store(); store.setStoreId(2L);

        when(staffRepo.findById(20L)).thenReturn(Optional.of(staff));
        when(storeRepo.findById(2L)).thenReturn(Optional.of(store));
        when(staffRepo.save(staff)).thenReturn(staff);
        stubModelMapperStaffToDTO();

        StaffDTO dto = staffService.updateStore(20L, 2L);

        assertThat(dto).isNotNull();
        assertThat(staff.getStore()).isEqualTo(store);
    }

    @Test
    void updateStore_nullStoreId_throwsBadRequest() {
        assertThrows(BadRequestException.class, () -> staffService.updateStore(20L, null));
    }

    @Test
    void updateStore_staffNotFound_throws() {
        when(staffRepo.findById(20L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> staffService.updateStore(20L, 2L));
    }

    @Test
    void updateStore_storeNotFound_throws() {
        Staff staff = staff(20L, "A", "B", "a@b.com");
        when(staffRepo.findById(20L)).thenReturn(Optional.of(staff));
        when(storeRepo.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> staffService.updateStore(20L, 2L));
    }

    // ---------------- updatePhoneNumberInAddress ----------------
    @Test
    void updatePhoneNumberInAddress_updatesPhone_whenAddressPresent() {
        Staff staff = staff(30L, "A", "B", "a@b.com");
        Address addr = new Address(); addr.setPhone("111");
        staff.setAddress(addr);

        when(staffRepo.findById(30L)).thenReturn(Optional.of(staff));
        when(staffRepo.save(staff)).thenReturn(staff);
        stubModelMapperStaffToDTO();

        StaffDTO dto = staffService.updatePhoneNumberInAddress(30L, "222");

        assertThat(dto).isNotNull();
        assertThat(staff.getAddress().getPhone()).isEqualTo("222");
        verify(staffRepo).save(staff);
    }

    @Test
    void updatePhoneNumberInAddress_noAddress_doesNotFail() {
        Staff staff = staff(31L, "A", "B", "a@b.com");
        staff.setAddress(null);

        when(staffRepo.findById(31L)).thenReturn(Optional.of(staff));
        when(staffRepo.save(staff)).thenReturn(staff);
        stubModelMapperStaffToDTO();

        StaffDTO dto = staffService.updatePhoneNumberInAddress(31L, "999");

        assertThat(dto).isNotNull();
        assertThat(staff.getAddress()).isNull();
        verify(staffRepo).save(staff);
    }

    // ---------------- assignAddress ----------------
    @Test
    void assignAddress_success() throws Exception {
        Staff staff = staff(40L, "A", "B", "a@b.com");
        Address address = new Address(); address.setAddressId(5L);

        when(staffRepo.findById(40L)).thenReturn(Optional.of(staff));
        when(addressRepo.findById(5L)).thenReturn(Optional.of(address));
        when(staffRepo.save(staff)).thenReturn(staff);
        stubModelMapperStaffToDTO();

        StaffDTO dto = staffService.assignAddress(40L, 5L);

        assertThat(dto).isNotNull();
        assertThat(staff.getAddress()).isEqualTo(address);
    }

    @Test
    void assignAddress_nullAddressId_throwsBadRequest() {
        assertThrows(BadRequestException.class, () -> staffService.assignAddress(40L, null));
    }

    @Test
    void assignAddress_staffNotFound_throws() {
        when(staffRepo.findById(40L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> staffService.assignAddress(40L, 5L));
    }

    @Test
    void assignAddress_addressNotFound_throws() {
        Staff staff = staff(40L, "A", "B", "a@b.com");
        when(staffRepo.findById(40L)).thenReturn(Optional.of(staff));
        when(addressRepo.findById(5L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> staffService.assignAddress(40L, 5L));
    }

    // ----------------------- Helpers -----------------------
    private static Staff staff(Long id, String first, String last, String email) {
        Staff s = new Staff();
        s.setStaffId(id);
        s.setFirstName(first);
        s.setLastName(last);
        s.setEmail(email);
        s.setLastupdate(LocalDateTime.now());
        return s;
    }

    /** Build staff with Address having a City (by city name) */
    private static Staff staffWithAddressCity(Long id, String first, String last, String email, String cityName) {
        Staff s = staff(id, first, last, email);
        Address a = new Address();
        City c = new City();
        // Adjust setter if your entity uses a different field name
        c.setCityName(cityName);
        a.setCity(c);
        s.setAddress(a);
        return s;
    }

    /** Build staff with Address having City → Country (by country name) */
    private static Staff staffWithAddressCountry(Long id, String first, String last, String email, String countryName) {
        Staff s = staff(id, first, last, email);
        Address a = new Address();
        Country country = new Country();
        country.setCountry(countryName);
        City c = new City();
        c.setCityName("DUMMY"); // city name not relevant for country filter
        c.setCountry(country);
        a.setCity(c);
        s.setAddress(a);
        return s;
    }

    /** Build staff with Address phone */
    private static Staff staffWithAddressPhone(Long id, String first, String last, String email, String phone) {
        Staff s = staff(id, first, last, email);
        Address a = new Address();
        a.setPhone(phone);
        s.setAddress(a);
        return s;
    }
}
