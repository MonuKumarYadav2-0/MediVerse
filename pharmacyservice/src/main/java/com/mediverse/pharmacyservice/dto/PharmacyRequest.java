package com.mediverse.pharmacyservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PharmacyRequest {

    @NotBlank(message = "Pharmacy name is required")
    @Size(max = 100, message = "Pharmacy name cannot exceed 100 characters")
    private String pharmacyName;

    @NotBlank(message = "Owner name is required")
    @Size(max = 100, message = "Owner name cannot exceed 100 characters")
    private String ownerName;

    @NotBlank(message = "Phone number is required")
    @Pattern(
        regexp = "^[6-9]\\d{9}$",
        message = "Invalid Indian phone number"
    )
    private String phone;

    @NotBlank(message = "Address is required")
    private String address;
}