package com.mediverse.pharmacyservice.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.mediverse.pharmacyservice.dto.PharmacyRequest;
import com.mediverse.pharmacyservice.dto.PharmacyResponse;
import com.mediverse.pharmacyservice.security.UserPrincipal;
import com.mediverse.pharmacyservice.service.PharmacyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/pharmacies")
@RequiredArgsConstructor
public class PharmacyController {

	private final PharmacyService pharmacyService;

	@PostMapping
	public ResponseEntity<PharmacyResponse> createPharmacy(@Valid @RequestBody PharmacyRequest request,
			Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		PharmacyResponse response = pharmacyService.createPharmacy(userId, request);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/me")
	public ResponseEntity<PharmacyResponse> getMyPharmacy(Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		return ResponseEntity.ok(pharmacyService.getMyPharmacy(userId));
	}

	@PutMapping("/me")
	public ResponseEntity<PharmacyResponse> updateMyPharmacy(@Valid @RequestBody PharmacyRequest request,
			Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		return ResponseEntity.ok(pharmacyService.updateMyPharmacy(userId, request));
	}
}