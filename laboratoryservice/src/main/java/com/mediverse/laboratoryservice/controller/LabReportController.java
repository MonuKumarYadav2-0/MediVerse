package com.mediverse.laboratoryservice.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.mediverse.laboratoryservice.dto.LabReportRequest;
import com.mediverse.laboratoryservice.dto.LabReportResponse;
import com.mediverse.laboratoryservice.security.UserPrincipal;
import com.mediverse.laboratoryservice.service.LabReportService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/lab-reports")
@RequiredArgsConstructor
public class LabReportController {

	private final LabReportService labReportService;

	// Laboratory uploads report
	@PostMapping("/booking/{bookingId}")
	public ResponseEntity<LabReportResponse> uploadReport(@PathVariable UUID bookingId,
			@Valid @RequestBody LabReportRequest request, Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		LabReportResponse response = labReportService.uploadReport(userId, bookingId, request);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// Patient views report
	@GetMapping("/booking/{bookingId}")
	public ResponseEntity<LabReportResponse> getReport(@PathVariable UUID bookingId, Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID patientId = principal.getUserId();

		return ResponseEntity.ok(labReportService.getReport(patientId, bookingId));
	}
}