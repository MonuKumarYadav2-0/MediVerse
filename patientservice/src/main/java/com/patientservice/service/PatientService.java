package com.patientservice.service;

import java.util.UUID;

import com.patientservice.dto.PatientRequest;
import com.patientservice.dto.PatientResponse;

public interface PatientService {

	PatientResponse createPatient(UUID userId, PatientRequest request);

	PatientResponse getPatientByUserId(UUID userId);

	PatientResponse getPatientById(UUID patientId);

	PatientResponse updatePatient(UUID userId, PatientRequest request);

	void deactivatePatient(UUID userId);
}