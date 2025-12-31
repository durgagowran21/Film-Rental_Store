package com.example.filmrental.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.filmrental.dto.StaffDTO;
import com.example.filmrental.dto.StaffAllUpdateRequestDto;
import com.example.filmrental.dto.StaffAllUpdateRequestDto.StaffAddressAssignRequest;
import com.example.filmrental.dto.StaffAllUpdateRequestDto.StaffEmailUpdateRequest;
import com.example.filmrental.dto.StaffAllUpdateRequestDto.StaffFirstNameUpdateRequest;
import com.example.filmrental.dto.StaffAllUpdateRequestDto.StaffLastNameUpdateRequest;
import com.example.filmrental.dto.StaffAllUpdateRequestDto.StaffPhoneUpdateRequest;
import com.example.filmrental.dto.StaffAllUpdateRequestDto.StaffStoreAssignRequest;
import com.example.filmrental.dto.StoreDto;
import com.example.filmrental.exception.ResourceNotFoundException;
import com.example.filmrental.model.Address;
import com.example.filmrental.model.Staff;
import com.example.filmrental.model.Store;
import com.example.filmrental.service.StaffService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/Staff")
public class StaffController {
	@Autowired
	private StaffService staffServ;
	
	@PostMapping
	public StaffDTO createStaff(@RequestBody StaffDTO staff) {
		return staffServ.addStaff(staff);
	}
	
	  @GetMapping("/{lastname}") 
	  public List<StaffDTO> getByLastName(@PathVariable String LastName) throws ResourceNotFoundException {
		  return staffServ.findStaffByLastName(LastName); }
	 

	@GetMapping("/{FirstName}")
	public List<StaffDTO> getbyfirstName(@PathVariable String firstName) throws ResourceNotFoundException {
		return staffServ.findStaffByFirstName(firstName);
	}
	
	  @GetMapping("/{Email}") 
	  public List<StaffDTO> getByEmail(@PathVariable String Email) throws ResourceNotFoundException {
	  return staffServ.findStaffByEmail(Email); }
	 


	  @GetMapping("/city/{city}")
	    public List<StaffDTO> findByAddressCityName(@PathVariable("city") String city) throws ResourceNotFoundException {
	        List<StaffDTO> staffList = staffServ.GetAllStaffBYCity(city);
	        return staffList;
	    }
	 
	    @GetMapping("/country/{country}")
	    public List<StaffDTO> findByAddress_City_Country_CountryName(@PathVariable String country) throws ResourceNotFoundException  {
	    	List<StaffDTO> staffList = staffServ.GetAllStaffBYCountry(country);
	        return staffList;
	    }
	 
	    @GetMapping("/phone/{phone}")
	    public List<StaffDTO> findByPhoneNumber(@PathVariable("phone") String phone) throws ResourceNotFoundException  {
	        List<StaffDTO> staffDTOList = staffServ.GetAllStaffBYPhone(phone);
			return staffDTOList;
	    }
	    

@PutMapping("/{id}/first-name")
public StaffDTO updateFirstName(@PathVariable Long id, @RequestBody StaffFirstNameUpdateRequest req) throws ResourceNotFoundException {
    return staffServ.updateFirstName(id, req.firstName());
}

@PutMapping("/{id}/last-name")
public StaffDTO updateLastName(@PathVariable Long id, @RequestBody StaffLastNameUpdateRequest req) throws ResourceNotFoundException {
    return staffServ.updateLastName(id, req.lastName());
}

@PutMapping("/{id}/email")
public StaffDTO updateEmail(@PathVariable Long id, @Valid @RequestBody StaffEmailUpdateRequest req ) throws ResourceNotFoundException {
    return staffServ.updateEmail(id,req.email());
}
@PutMapping("/update/phone/{id}")
public ResponseEntity<StaffDTO> updatePhoneNumberInAddress(@PathVariable("id") Long id, @RequestBody StaffPhoneUpdateRequest req) throws ResourceNotFoundException {
    StaffDTO updatedStaff = staffServ.updatePhoneNumberInAddress(id, req.phone());
    return new ResponseEntity<>(updatedStaff, HttpStatus.OK);
}



@PutMapping("/api/staff/update/address/{id}")
public ResponseEntity<StaffDTO> assignAddress(
        @PathVariable Long id,
        @Valid @RequestBody StaffAddressAssignRequest req) throws ResourceNotFoundException {

    StaffDTO dto = staffServ.assignAddress(id, req.addressId());
    return ResponseEntity.ok(dto); // 200 OK on success
}



@PutMapping("/api/staff/update/store/{id}")
public ResponseEntity<StaffDTO> assignStore(
        @PathVariable Long id,
        @Valid @RequestBody StaffStoreAssignRequest req) throws ResourceNotFoundException {

    StaffDTO dto = staffServ.updateStore(id, req.storeId());
    return ResponseEntity.ok(dto);
}


}

	

