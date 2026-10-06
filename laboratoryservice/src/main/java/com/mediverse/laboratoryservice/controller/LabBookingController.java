package com.mediverse.laboratoryservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.mediverse.laboratoryservice.dto.LabBookingRequest;
import com.mediverse.laboratoryservice.dto.LabBookingResponse;
import com.mediverse.laboratoryservice.entity.BookingStatus;
import com.mediverse.laboratoryservice.security.UserPrincipal;
import com.mediverse.laboratoryservice.service.LabBookingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/lab-bookings")
@RequiredArgsConstructor
public class LabBookingController {

	private final LabBookingService labBookingService;

	// Patient creates booking
	@PostMapping
	public ResponseEntity<LabBookingResponse> createBooking(@Valid @RequestBody LabBookingRequest request,
			Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID patientId = principal.getUserId();

		return ResponseEntity.ok(labBookingService.createBooking(patientId, request));
	}

	// Patient gets all own bookings
	@GetMapping("/me")
	public ResponseEntity<List<LabBookingResponse>> getMyBookings(Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID patientId = principal.getUserId();

		return ResponseEntity.ok(labBookingService.getMyBookings(patientId));
	}

	// Patient gets particular booking
	@GetMapping("/{bookingId}")
	public ResponseEntity<LabBookingResponse> getBooking(@PathVariable UUID bookingId, Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID patientId = principal.getUserId();

		return ResponseEntity.ok(labBookingService.getBooking(patientId, bookingId));
	}

	// Laboratory gets all its bookings
	@GetMapping("/laboratory")
	public ResponseEntity<List<LabBookingResponse>> getLaboratoryBookings(Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		return ResponseEntity.ok(labBookingService.getLaboratoryBookings(userId));
	}

	// Laboratory updates booking status
	@PatchMapping("/{bookingId}/status")
	public ResponseEntity<LabBookingResponse> updateStatus(@PathVariable UUID bookingId,
			@RequestParam BookingStatus status, Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		return ResponseEntity.ok(labBookingService.updateStatus(userId, bookingId, status));
	}
}