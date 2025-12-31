package com.example.filmrental.service;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.filmrental.dto.StaffDTO;
import com.example.filmrental.exception.BadRequestException;
import com.example.filmrental.exception.ResourceNotFoundException;
import com.example.filmrental.model.Address;
import com.example.filmrental.model.Staff;
import com.example.filmrental.model.Store;
import com.example.filmrental.repository.AddressRepository;
import com.example.filmrental.repository.StaffRepository;
import com.example.filmrental.repository.StoreRepository;

import jakarta.transaction.Transactional;


@Service
public class StaffService {
	@Autowired
	private StaffRepository staffRepo;
	
	@Autowired
	private StoreRepository storeRepo;
	@Autowired
	private AddressRepository addressrepo;
	@Autowired
	private ModelMapper modelMapper;
	 public List<StaffDTO> findStaffByLastName(String lastName) throws ResourceNotFoundException {
	        List<Staff> staffList = staffRepo.findByLastName(lastName);
	        if (staffList.isEmpty()) {
	            throw new ResourceNotFoundException("No staff found with last name: " + lastName);
	        }
	 
	        List<StaffDTO> staffDTOList = new ArrayList<>();
	        for (Staff staff : staffList) {
	            staffDTOList.add(modelMapper.map(staff, StaffDTO.class));
	        }
	        return staffDTOList;
	    }
	
	 public List<StaffDTO> findStaffByFirstName(String firstName) throws ResourceNotFoundException {
	        List<Staff> staffList = staffRepo.findByFirstName(firstName);
	        if (staffList.isEmpty()) {
	            throw new ResourceNotFoundException("No staff found with first name: " + firstName);
	        }
	 
	        List<StaffDTO> staffDTOList = new ArrayList<>();
	        for (Staff staff : staffList) {
	            staffDTOList.add(modelMapper.map(staff, StaffDTO.class));
	        }
	        return staffDTOList;
	    }
	 
	    // Find staff by email
	    public List<StaffDTO> findStaffByEmail(String email) throws ResourceNotFoundException {
	        List<Staff> staffList = staffRepo.findOneByEmail(email);
	        if (staffList.isEmpty()) {
	            throw new ResourceNotFoundException("No staff found with email: " + email);
	        }
	 
	        List<StaffDTO> staffDTOList = new ArrayList<>();
	        for (Staff staff : staffList) {
	            staffDTOList.add(modelMapper.map(staff, StaffDTO.class));
	        }
	        return staffDTOList;
	    }
	
	public StaffDTO addStaff(StaffDTO staffDTO) {
        Staff staff = modelMapper.map(staffDTO, Staff.class);
        Staff savedStaff = staffRepo.save(staff);
        return modelMapper.map(savedStaff, StaffDTO.class);
    }

	public List<StaffDTO> GetAllStaffBYCity(String city) throws ResourceNotFoundException{
		List<Staff> staffList = staffRepo.findOneByCity(city);
        if (staffList.isEmpty()) {
            throw new ResourceNotFoundException("No staff found in city: " + city);
        }
 
        List<StaffDTO> staffDTOList = new ArrayList<>();
        for (Staff staff : staffList) {
            staffDTOList.add(modelMapper.map(staff, StaffDTO.class));
        }
        return staffDTOList;
	}
	
	public List<StaffDTO> GetAllStaffBYCountry(String country) throws com.example.filmrental.exception.ResourceNotFoundException{
		List<Staff> staffList = staffRepo.findOneByCountry(country);
        if (staffList.isEmpty()) {
            throw new ResourceNotFoundException("No staff found in country: " + country);
        }
 
        List<StaffDTO> staffDTOList = new ArrayList<>();
        for (Staff staff : staffList) {
            staffDTOList.add(modelMapper.map(staff, StaffDTO.class));
        }
        return staffDTOList;
	}
	
	public List<StaffDTO> GetAllStaffBYPhone(String phone) throws com.example.filmrental.exception.ResourceNotFoundException{
		List<Staff> staffList = staffRepo.findOneByPhone(phone);
        if (staffList.isEmpty()) {
            throw new ResourceNotFoundException("No staff found with phone number: " + phone);
        }
 
        List<StaffDTO> staffDTOList = new ArrayList<>();
        for (Staff staff : staffList) {
            staffDTOList.add(modelMapper.map(staff, StaffDTO.class));
        }
        return staffDTOList;
	}
	@Transactional
	 public StaffDTO updateFirstName(Long id, String firstName) throws ResourceNotFoundException {
	        Staff staff = staffRepo.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + id));
	        staff.setFirstName(firstName);
	        Staff updatedStaff = staffRepo.save(staff);
	        return modelMapper.map(updatedStaff, StaffDTO.class);
	    }
	 
	    // Update staff last name
	 @Transactional
	    public StaffDTO updateLastName(Long id, String lastName) throws ResourceNotFoundException {
	        Staff staff = staffRepo.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + id));
	        staff.setLastName(lastName);
	        Staff updatedStaff = staffRepo.save(staff);
	        return modelMapper.map(updatedStaff, StaffDTO.class);
	    }
	 
	    // Update staff email
	    @Transactional
	    public StaffDTO updateEmail(Long id, String email) throws ResourceNotFoundException {
	        Staff staff = staffRepo.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + id));
	        staff.setEmail(email);
	        Staff updatedStaff = staffRepo.save(staff);
	        return modelMapper.map(updatedStaff, StaffDTO.class);
	    }
	 



@Transactional
public StaffDTO updateStore(Long staffId, Long storeId) throws ResourceNotFoundException {
    if (storeId == null) {
        throw new BadRequestException("storeId must not be null");
    }

    Staff staff = staffRepo.findById(staffId)
        .orElseThrow(() -> new ResourceNotFoundException("Staff not found: " + staffId));

    Store store = storeRepo.findById(storeId)
        .orElseThrow(() -> new ResourceNotFoundException("Store not found: " + storeId));

    staff.setStore(store);
    Staff updated = staffRepo.save(staff);
    return modelMapper.map(updated, StaffDTO.class);
}

        @Transactional
        public StaffDTO updatePhoneNumberInAddress(Long staffId, String phoneNumber)  {
            Staff staff = staffRepo.findById(staffId).orElse(null);
     
            Address address = staff.getAddress();
            if (address != null) {
                address.setPhone(phoneNumber);
            } 
     
            staffRepo.save(staff);
            return modelMapper.map(staff, StaffDTO.class);
        }
        

@Transactional
public StaffDTO assignAddress(Long staffId, Long addressId) throws ResourceNotFoundException {
    if (addressId == null) {
        throw new BadRequestException("addressId must not be null");
    }

    Staff staff = staffRepo.findById(staffId)
        .orElseThrow(() -> new ResourceNotFoundException("Staff not found: " + staffId));

    Address address = addressrepo.findById(addressId)
        .orElseThrow(() -> new ResourceNotFoundException("Address not found: " + addressId));

    staff.setAddress(address);
    Staff updatedStaff = staffRepo.save(staff);
    return modelMapper.map(updatedStaff, StaffDTO.class);
}

}

