package com.mediverse.doctorservice.service;

import java.util.List;
import java.util.UUID;

import com.mediverse.doctorservice.dto.DoctorAvailabilityResponse;
import com.mediverse.doctorservice.dto.DoctorRequest;
import com.mediverse.doctorservice.dto.DoctorResponse;

public interface DoctorService {

	DoctorResponse createDoctor(UUID userId, DoctorRequest request);

	DoctorResponse getCurrentDoctor(UUID userId);

	DoctorResponse updateDoctor(UUID userId, DoctorRequest request);

	DoctorResponse getDoctorById(UUID doctorId);

	void deleteCurrentDoctor(UUID userId);

}