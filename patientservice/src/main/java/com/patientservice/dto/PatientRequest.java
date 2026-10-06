package com.patientservice.dto;

import java.time.LocalDate;

import com.patientservice.entity.Gender;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientRequest {

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @Pattern(
        regexp = "^(A|B|AB|O)[+-]$",
        message = "Invalid blood group"
    )
    private String bloodGroup;

    @Size(max = 500, message = "Address cannot exceed 500 characters")
    private String address;

    @Size(max = 100, message = "City cannot exceed 100 characters")
    private String city;

    @Size(max = 100, message = "State cannot exceed 100 characters")
    private String state;

    @Pattern(
        regexp = "^[0-9]{6}$",
        message = "Pincode must contain exactly 6 digits"
    )
    private String pincode;

    @Size(max = 100, message = "Emergency contact name cannot exceed 100 characters")
    private String emergencyContactName;

    @Pattern(
        regexp = "^\\+?[0-9]{10,15}$",
        message = "Invalid emergency contact number"
    )
    private String emergencyContactPhone;
}