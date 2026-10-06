package com.mediverse.pharmacyservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mediverse.pharmacyservice.dto.ExternalPrescriptionResponse;
import com.mediverse.pharmacyservice.security.UserPrincipal;
import com.mediverse.pharmacyservice.service.ExternalPrescriptionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/external-prescriptions")
@RequiredArgsConstructor
public class ExternalPrescriptionController {

	private final ExternalPrescriptionService externalPrescriptionService;

	@PostMapping("/upload")
	public ResponseEntity<ExternalPrescriptionResponse> uploadPrescription(@RequestParam("file") MultipartFile file,
			Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID patientId = principal.getUserId();

		ExternalPrescriptionResponse response = externalPrescriptionService.uploadPrescription(patientId, file);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/me")
	public ResponseEntity<List<ExternalPrescriptionResponse>> getMyPrescriptions(Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID patientId = principal.getUserId();

		return ResponseEntity.ok(externalPrescriptionService.getMyPrescriptions(patientId));
	}
}