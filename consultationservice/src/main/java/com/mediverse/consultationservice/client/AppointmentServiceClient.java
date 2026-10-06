package com.mediverse.consultationservice.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.mediverse.consultationservice.dto.AppointmentResponse;

@FeignClient(name = "appointment-service")
public interface AppointmentServiceClient {

	@GetMapping("/api/v1/appointments/{appointmentId}")
	AppointmentResponse getAppointment(@PathVariable("appointmentId") UUID appointmentId,
			@RequestHeader("Authorization") String token);
}