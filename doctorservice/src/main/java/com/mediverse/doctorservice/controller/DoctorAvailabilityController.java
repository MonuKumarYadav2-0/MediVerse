package com.mediverse.doctorservice.controller;

import java.time.DayOfWeek;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mediverse.doctorservice.dto.DoctorAvailabilityRequest;
import com.mediverse.doctorservice.dto.DoctorAvailabilityResponse;
import com.mediverse.doctorservice.security.UserPrincipal;
import com.mediverse.doctorservice.service.DoctorAvailabilityService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorAvailabilityController {

	private final DoctorAvailabilityService availabilityService;

	// Create availability for logged-in doctor
	@PostMapping("/me/availability")
	public ResponseEntity<DoctorAvailabilityResponse> createAvailability(
			@Valid @RequestBody DoctorAvailabilityRequest request, Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		DoctorAvailabilityResponse response = availabilityService.createAvailability(userId, request);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// Get availability of logged-in doctor
	@GetMapping("/me/availability")
	public ResponseEntity<List<DoctorAvailabilityResponse>> getMyAvailability(Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		List<DoctorAvailabilityResponse> response = availabilityService.getMyAvailability(userId);

		return ResponseEntity.ok(response);
	}

	// Update availability of logged-in doctor
	@PutMapping("/me/availability/{availabilityId}")
	public ResponseEntity<DoctorAvailabilityResponse> updateAvailability(@PathVariable UUID availabilityId,
			@Valid @RequestBody DoctorAvailabilityRequest request, Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		DoctorAvailabilityResponse response = availabilityService.updateAvailability(userId, availabilityId, request);

		return ResponseEntity.ok(response);
	}

	// Delete availability of logged-in doctor
	@DeleteMapping("/me/availability/{availabilityId}")
	public ResponseEntity<Void> deleteAvailability(@PathVariable UUID availabilityId, Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		availabilityService.deleteAvailability(userId, availabilityId);

		return ResponseEntity.noContent().build();
	}

	// Get a specific doctor's availability for a particular day
	@GetMapping("/{doctorId}/availability")
	public ResponseEntity<DoctorAvailabilityResponse> getDoctorAvailability(@PathVariable UUID doctorId,
			@RequestParam DayOfWeek dayOfWeek) {

		DoctorAvailabilityResponse response = availabilityService.getDoctorAvailability(doctorId, dayOfWeek);

		return ResponseEntity.ok(response);
	}
}