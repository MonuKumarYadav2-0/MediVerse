package com.patientservice.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.patientservice.dto.PatientRequest;
import com.patientservice.dto.PatientResponse;
import com.patientservice.security.UserPrincipal;
import com.patientservice.service.PatientService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {

	private final PatientService patientService;

	@PostMapping
	public ResponseEntity<PatientResponse> createPatient(Authentication authentication,
			@Valid @RequestBody PatientRequest request) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		PatientResponse response = patientService.createPatient(userId, request);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/me")
	public ResponseEntity<PatientResponse> getMyProfile(Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		return ResponseEntity.ok(patientService.getPatientByUserId(userId));
	}

	@PutMapping("/me")
	public ResponseEntity<PatientResponse> updateMyProfile(Authentication authentication,
			@Valid @RequestBody PatientRequest request) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		return ResponseEntity.ok(patientService.updatePatient(userId, request));
	}

	@GetMapping("/{patientId}")
	public ResponseEntity<PatientResponse> getPatientById(@PathVariable UUID patientId) {

		return ResponseEntity.ok(patientService.getPatientById(patientId));
	}

	@DeleteMapping("/me")
	public ResponseEntity<Void> deactivateMyProfile(Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		patientService.deactivatePatient(userId);

		return ResponseEntity.noContent().build();
	}
}