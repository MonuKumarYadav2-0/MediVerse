package com.mediverse.doctorservice.mapper;

import org.springframework.stereotype.Component;

import com.mediverse.doctorservice.dto.DoctorAvailabilityRequest;
import com.mediverse.doctorservice.dto.DoctorAvailabilityResponse;
import com.mediverse.doctorservice.entity.DoctorAvailability;

@Component
public class DoctorAvailabilityMapper {

    public DoctorAvailability toEntity(
            DoctorAvailabilityRequest request) {

        return DoctorAvailability.builder()
                .dayOfWeek(request.getDayOfWeek())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .available(request.getAvailable())
                .build();
    }

    public DoctorAvailabilityResponse toResponse(
            DoctorAvailability availability) {

        return DoctorAvailabilityResponse.builder()
                .id(availability.getId())
                .doctorId(availability.getDoctorId())
                .dayOfWeek(availability.getDayOfWeek())
                .startTime(availability.getStartTime())
                .endTime(availability.getEndTime())
                .available(availability.getAvailable())
                .build();
    }

    public void updateEntity(
            DoctorAvailability availability,
            DoctorAvailabilityRequest request) {

        availability.setDayOfWeek(request.getDayOfWeek());
        availability.setStartTime(request.getStartTime());
        availability.setEndTime(request.getEndTime());
        availability.setAvailable(request.getAvailable());
    }
}