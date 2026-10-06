package com.patientservice.mapper;

import org.springframework.stereotype.Component;

import com.patientservice.dto.PatientRequest;
import com.patientservice.dto.PatientResponse;
import com.patientservice.entity.Patient;

@Component
public class PatientMapper {

    public Patient toEntity(PatientRequest request) {

        return Patient.builder()
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .bloodGroup(request.getBloodGroup())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
				.emergencyContactName(request.getEmergencyContactName())
				.emergencyContactPhone(request.getEmergencyContactPhone())
                .build();
    }

    public PatientResponse toResponse(Patient patient) {

        return PatientResponse.builder()
                .id(patient.getId())
                .userId(patient.getUserId())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .bloodGroup(patient.getBloodGroup())
                .address(patient.getAddress())
                .city(patient.getCity())
                .state(patient.getState())
                .pincode(patient.getPincode())
				.emergencyContactName(patient.getEmergencyContactName())
				.emergencyContactPhone(patient.getEmergencyContactPhone())
                .active(patient.isActive())
                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())
                .build();
    }
}