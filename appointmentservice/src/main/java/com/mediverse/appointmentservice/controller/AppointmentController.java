package com.mediverse.appointmentservice.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.mediverse.appointmentservice.dto.AppointmentRequest;
import com.mediverse.appointmentservice.dto.AppointmentResponse;
import com.mediverse.appointmentservice.security.UserPrincipal;
import com.mediverse.appointmentservice.service.AppointmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

	private final AppointmentService appointmentService;

	// Patient books appointment
	@PostMapping
	public ResponseEntity<AppointmentResponse> createAppointment(@Valid @RequestBody AppointmentRequest request,
			Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID patientId = principal.getUserId();

		AppointmentResponse response = appointmentService.createAppointment(patientId, request);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// Patient gets own appointments
	@GetMapping("/me")
	public ResponseEntity<List<AppointmentResponse>> getMyAppointments(Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID patientId = principal.getUserId();

		List<AppointmentResponse> response = appointmentService.getMyAppointments(patientId);

		return ResponseEntity.ok(response);
	}

	// Get appointment by ID
	@GetMapping("/{appointmentId}")
	public ResponseEntity<AppointmentResponse> getAppointmentById(@PathVariable UUID appointmentId) {

		AppointmentResponse response = appointmentService.getAppointmentById(appointmentId);

		return ResponseEntity.ok(response);
	}

	// Patient cancels appointment
	@DeleteMapping("/{appointmentId}")
	public ResponseEntity<Void> cancelAppointment(@PathVariable UUID appointmentId, Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID patientId = principal.getUserId();

		appointmentService.cancelAppointment(patientId, appointmentId);

		return ResponseEntity.noContent().build();
	}

	// Doctor gets own appointments
	@GetMapping("/doctor")
	public ResponseEntity<List<AppointmentResponse>> getDoctorAppointments(Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID doctorId = principal.getUserId();

		List<AppointmentResponse> response = appointmentService.getDoctorAppointments(doctorId);
		System.out.println(doctorId);

		return ResponseEntity.ok(response);
	}

	// Doctor confirms appointment
	@PutMapping("/{appointmentId}/confirm")
	public ResponseEntity<AppointmentResponse> confirmAppointment(@PathVariable UUID appointmentId,
			Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID doctorId = principal.getUserId();

		AppointmentResponse response = appointmentService.confirmAppointment(doctorId, appointmentId);

		return ResponseEntity.ok(response);
	}

	// Doctor completes appointment
	@PutMapping("/{appointmentId}/complete")
	public ResponseEntity<AppointmentResponse> completeAppointment(@PathVariable UUID appointmentId,
			Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID doctorId = principal.getUserId();

		AppointmentResponse response = appointmentService.completeAppointment(doctorId, appointmentId);

		return ResponseEntity.ok(response);
	}
}