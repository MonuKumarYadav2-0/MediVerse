package com.mediverse.doctorservice.controller;

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

import com.mediverse.doctorservice.dto.DoctorRequest;
import com.mediverse.doctorservice.dto.DoctorResponse;
import com.mediverse.doctorservice.security.UserPrincipal;
import com.mediverse.doctorservice.service.DoctorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {

	private final DoctorService doctorService;

	@PostMapping
	public ResponseEntity<DoctorResponse> createDoctor(@Valid @RequestBody DoctorRequest request,
			Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		DoctorResponse response = doctorService.createDoctor(userId, request);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/me")
	public ResponseEntity<DoctorResponse> getCurrentDoctor(Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		DoctorResponse response = doctorService.getCurrentDoctor(userId);

		return ResponseEntity.ok(response);
	}

	@PutMapping("/me")
	public ResponseEntity<DoctorResponse> updateDoctor(@Valid @RequestBody DoctorRequest request,
			Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		DoctorResponse response = doctorService.updateDoctor(userId, request);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/{doctorId}")
	public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable UUID doctorId) {

		DoctorResponse response = doctorService.getDoctorById(doctorId);

		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/me")
	public ResponseEntity<Void> deleteCurrentDoctor(Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		doctorService.deleteCurrentDoctor(userId);

		return ResponseEntity.noContent().build();
	}

	
}