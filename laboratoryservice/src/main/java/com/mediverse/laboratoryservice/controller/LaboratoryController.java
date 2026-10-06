package com.mediverse.laboratoryservice.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.mediverse.laboratoryservice.dto.LaboratoryRequest;
import com.mediverse.laboratoryservice.dto.LaboratoryResponse;
import com.mediverse.laboratoryservice.security.UserPrincipal;
import com.mediverse.laboratoryservice.service.LaboratoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/laboratories")
@RequiredArgsConstructor
public class LaboratoryController {

	private final LaboratoryService laboratoryService;

	@PostMapping
	public ResponseEntity<LaboratoryResponse> registerLab(@Valid @RequestBody LaboratoryRequest request,
			Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		LaboratoryResponse response = laboratoryService.registerLab(userId, request);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/me")
	public ResponseEntity<LaboratoryResponse> getMyLab(Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
       
		UUID userId = principal.getUserId();
		System.out.println(userId);

		return ResponseEntity.ok(laboratoryService.getMyLab(userId));
	}

	@PutMapping("/me")
	public ResponseEntity<LaboratoryResponse> updateMyLab(@Valid @RequestBody LaboratoryRequest request,
			Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();
		System.out.println(userId);
		return ResponseEntity.ok(laboratoryService.updateMyLab(userId, request));
	}
}