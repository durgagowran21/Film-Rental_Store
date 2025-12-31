
package com.example.filmrental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class StaffAllUpdateRequestDto
{
	public record StaffFirstNameUpdateRequest(
    @NotBlank(message = "First name is required")
    String firstName
    ) {}
	public record StaffLastNameUpdateRequest(
    @NotBlank(message = "Last name is required")
    String lastName
    ) {}
	public record StaffEmailUpdateRequest(
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email
    ) {}
	public record StaffPhoneUpdateRequest(
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits")
    String phone
    ) {}
	public record StaffStoreAssignRequest(
    @NotNull(message = "storeId is required")
    Long storeId
    ) {}
	public record StaffAddressAssignRequest(
    @NotNull(message = "addressId is required")
    Long addressId
    ) {}












}
