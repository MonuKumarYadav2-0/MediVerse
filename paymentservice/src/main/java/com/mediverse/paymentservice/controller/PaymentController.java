package com.mediverse.paymentservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.mediverse.paymentservice.dto.PaymentRequest;
import com.mediverse.paymentservice.dto.PaymentResponse;
import com.mediverse.paymentservice.entity.PaymentStatus;
import com.mediverse.paymentservice.security.UserPrincipal;
import com.mediverse.paymentservice.service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	// ==========================================
	// PATIENT - CREATE PAYMENT
	// ==========================================

	@PostMapping
	public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request,
			Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID patientId = principal.getUserId();

		PaymentResponse response = paymentService.createPayment(patientId, request);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// ==========================================
	// PATIENT - GET PARTICULAR PAYMENT
	// ==========================================

	@GetMapping("/{paymentId}")
	public ResponseEntity<PaymentResponse> getPayment(@PathVariable UUID paymentId, Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID patientId = principal.getUserId();

		return ResponseEntity.ok(paymentService.getPayment(patientId, paymentId));
	}

	// ==========================================
	// PATIENT - GET MY PAYMENTS
	// ==========================================

	@GetMapping("/me")
	public ResponseEntity<List<PaymentResponse>> getMyPayments(Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID patientId = principal.getUserId();

		return ResponseEntity.ok(paymentService.getMyPayments(patientId));
	}

	// ==========================================
	// UPDATE PAYMENT STATUS
	// ==========================================

	@PatchMapping("/{paymentId}/status")
	public ResponseEntity<PaymentResponse> updatePaymentStatus(@PathVariable UUID paymentId,
			@RequestParam PaymentStatus status) {

		return ResponseEntity.ok(paymentService.updatePaymentStatus(paymentId, status));
	}
}