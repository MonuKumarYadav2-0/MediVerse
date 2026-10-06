package com.patientservice.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.patientservice.entity.Gender;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PatientResponse {

    private UUID id;

    private UUID userId;

    private LocalDate dateOfBirth;

    private Gender gender;

    private String bloodGroup;

    private String address;

    private String city;

    private String state;

    private String pincode;

    private String emergencyContactName;

    private String emergencyContactPhone;

    private boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}