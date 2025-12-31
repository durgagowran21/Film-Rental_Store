
package com.example.filmrental.dto;

import lombok.Data;

@Data
public class ManagerSummaryDto {
    private Long storeId;
    private Long managerStaffId;
    private String managerFirstName;
    private String managerLastName;
    private String managerEmail;
    private String managerPhone;
    private String storePhone;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String country;
}
