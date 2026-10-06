package com.mediverse.prescriptionservice.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.mediverse.prescriptionservice.dto.ConsultationResponse;

@FeignClient(name = "consultation-service")
public interface ConsultationServiceClient {

	@GetMapping("/api/v1/consultations/{consultationId}")
	ConsultationResponse getConsultation(@PathVariable("consultationId") UUID consultationId,
			@RequestHeader("Authorization") String token);
}