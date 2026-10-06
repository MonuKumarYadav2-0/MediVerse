package com.mediverse.pharmacyservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.mediverse.pharmacyservice.dto.MedicineRequest;
import com.mediverse.pharmacyservice.dto.MedicineResponse;
import com.mediverse.pharmacyservice.security.UserPrincipal;
import com.mediverse.pharmacyservice.service.MedicineService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/medicines")
@RequiredArgsConstructor
public class MedicineController {

	private final MedicineService medicineService;

	@PostMapping
	public ResponseEntity<MedicineResponse> addMedicine(@Valid @RequestBody MedicineRequest request,
			Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		MedicineResponse response = medicineService.addMedicine(userId, request);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/me")
	public ResponseEntity<List<MedicineResponse>> getMyMedicines(Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		return ResponseEntity.ok(medicineService.getMyMedicines(userId));
	}

	@PutMapping("/{medicineId}")
	public ResponseEntity<MedicineResponse> updateMedicine(@PathVariable UUID medicineId,
			@Valid @RequestBody MedicineRequest request, Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		return ResponseEntity.ok(medicineService.updateMedicine(userId, medicineId, request));
	}

	@GetMapping("/search")
	public ResponseEntity<List<MedicineResponse>> searchMedicines(@RequestParam String name) {

		return ResponseEntity.ok(medicineService.searchMedicines(name));
	}

	@DeleteMapping("/{medicineId}")
	public ResponseEntity<Void> deactivateMedicine(@PathVariable UUID medicineId, Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		medicineService.deactivateMedicine(userId, medicineId);

		return ResponseEntity.noContent().build();
	}
	
}