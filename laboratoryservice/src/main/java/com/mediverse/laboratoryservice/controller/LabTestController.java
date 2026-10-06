package com.mediverse.laboratoryservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.mediverse.laboratoryservice.dto.LabTestRequest;
import com.mediverse.laboratoryservice.dto.LabTestResponse;
import com.mediverse.laboratoryservice.security.UserPrincipal;
import com.mediverse.laboratoryservice.service.LabTestService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/lab-tests")
@RequiredArgsConstructor
public class LabTestController {

	private final LabTestService labTestService;

	// Lab adds a test
	@PostMapping
	public ResponseEntity<LabTestResponse> addTest(@Valid @RequestBody LabTestRequest request,
			Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		LabTestResponse response = labTestService.addTest(userId, request);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// Lab gets its own tests
	@GetMapping("/me")
	public ResponseEntity<List<LabTestResponse>> getMyTests(Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		return ResponseEntity.ok(labTestService.getMyTests(userId));
	}

	// Patient can search tests
	@GetMapping("/search")
	public ResponseEntity<List<LabTestResponse>> searchTests(@RequestParam String name) {

		return ResponseEntity.ok(labTestService.searchTests(name));
	}

	// Lab updates its own test
	@PutMapping("/{testId}")
	public ResponseEntity<LabTestResponse> updateTest(@PathVariable UUID testId,
			@Valid @RequestBody LabTestRequest request, Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		return ResponseEntity.ok(labTestService.updateTest(userId, testId, request));
	}

	// Lab deactivates its own test
	@DeleteMapping("/{testId}")
	public ResponseEntity<Void> deactivateTest(@PathVariable UUID testId, Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		labTestService.deactivateTest(userId, testId);

		return ResponseEntity.noContent().build();
	}
}