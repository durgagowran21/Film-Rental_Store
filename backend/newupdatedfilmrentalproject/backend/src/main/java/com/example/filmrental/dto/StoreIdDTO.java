package com.example.filmrental.dto;



import lombok.Data;
 
@Data
public class StoreIdDTO {
 
	    private Long storeId;
	    
	    private String phone;
 
	    public StoreIdDTO(Long storeId,String phone) {
	        this.storeId = storeId;
	        this.phone=phone;
	    }
 
}
 
 