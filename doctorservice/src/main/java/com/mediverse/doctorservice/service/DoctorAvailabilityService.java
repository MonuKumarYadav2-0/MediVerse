package com.mediverse.doctorservice.service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

import com.mediverse.doctorservice.dto.DoctorAvailabilityRequest;
import com.mediverse.doctorservice.dto.DoctorAvailabilityResponse;

public interface DoctorAvailabilityService {

    DoctorAvailabilityResponse createAvailability(
            UUID userId,
            DoctorAvailabilityRequest request
    );

    List<DoctorAvailabilityResponse> getMyAvailability(
            UUID userId
    );

    DoctorAvailabilityResponse updateAvailability(
            UUID userId,
            UUID availabilityId,
            DoctorAvailabilityRequest request
    );

    void deleteAvailability(
            UUID userId,
            UUID availabilityId
    );

	List<DoctorAvailabilityResponse> getDoctorAvailability(UUID doctorId);

	DoctorAvailabilityResponse getDoctorAvailability(UUID doctorId, DayOfWeek dayOfWeek);
}