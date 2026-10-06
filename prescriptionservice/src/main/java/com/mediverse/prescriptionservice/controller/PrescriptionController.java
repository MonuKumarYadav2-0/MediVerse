package com.mediverse.prescriptionservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediverse.prescriptionservice.dto.PrescriptionRequest;
import com.mediverse.prescriptionservice.dto.PrescriptionResponse;
import com.mediverse.prescriptionservice.security.UserPrincipal;
import com.mediverse.prescriptionservice.service.PrescriptionService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

	private final PrescriptionService prescriptionService;

	// Doctor creates prescription
	@PostMapping
	public ResponseEntity<PrescriptionResponse> createPrescription(@Valid @RequestBody PrescriptionRequest request,
			Authentication authentication, HttpServletRequest httpRequest) {

		System.out.println("=================");

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID doctorId = principal.getUserId();

		// Get original JWT from incoming request
		String token = httpRequest.getHeader("Authorization");

		PrescriptionResponse response = prescriptionService.createPrescription(doctorId, request, token);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// Patient gets own prescriptions
	@GetMapping("/me")
	public ResponseEntity<List<PrescriptionResponse>> getMyPrescriptions(Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID patientId = principal.getUserId();

		List<PrescriptionResponse> response = prescriptionService.getMyPrescriptions(patientId);

		return ResponseEntity.ok(response);
	}

	// Doctor gets prescriptions created by them
	@GetMapping("/doctor")
	public ResponseEntity<List<PrescriptionResponse>> getDoctorPrescriptions(Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID doctorId = principal.getUserId();

		List<PrescriptionResponse> response = prescriptionService.getDoctorPrescriptions(doctorId);

		return ResponseEntity.ok(response);
	}

	// Get prescription by ID
	@GetMapping("/{prescriptionId}")
	public ResponseEntity<PrescriptionResponse> getPrescriptionById(@PathVariable UUID prescriptionId) {

		PrescriptionResponse response = prescriptionService.getPrescriptionById(prescriptionId);

		return ResponseEntity.ok(response);
	}
}