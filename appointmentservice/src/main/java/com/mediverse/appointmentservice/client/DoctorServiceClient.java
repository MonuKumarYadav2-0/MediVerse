package com.mediverse.appointmentservice.client;

import java.time.DayOfWeek;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.mediverse.appointmentservice.dto.DoctorAvailabilityResponse;
import com.mediverse.appointmentservice.dto.DoctorResponse;

@FeignClient(name = "doctor-service")
public interface DoctorServiceClient {

	@GetMapping("/api/v1/doctors/{doctorId}/availability")
	DoctorAvailabilityResponse getDoctorAvailability(@PathVariable("doctorId") UUID doctorId,
			@RequestParam("dayOfWeek") DayOfWeek dayOfWeek);

	@GetMapping("/api/v1/doctors/{doctorId}")
	DoctorResponse getDoctor(@PathVariable("doctorId") UUID doctorId);
}