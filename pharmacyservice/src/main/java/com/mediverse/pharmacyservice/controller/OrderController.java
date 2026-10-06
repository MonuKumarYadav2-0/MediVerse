package com.mediverse.pharmacyservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.mediverse.pharmacyservice.dto.CreateOrderRequest;
import com.mediverse.pharmacyservice.dto.OrderResponse;
import com.mediverse.pharmacyservice.entity.OrderStatus;
import com.mediverse.pharmacyservice.security.UserPrincipal;
import com.mediverse.pharmacyservice.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	// =========================================================
	// PATIENT SIDE
	// =========================================================

	// 1. Create Order
	@PostMapping
	public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request,
			Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID patientId = principal.getUserId();

		OrderResponse response = orderService.createOrder(patientId, request);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// 2. Get My Orders
	@GetMapping("/me")
	public ResponseEntity<List<OrderResponse>> getMyOrders(Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID patientId = principal.getUserId();

		return ResponseEntity.ok(orderService.getMyOrders(patientId));
	}

	// 3. Get Particular Order - Patient
	@GetMapping("/{orderId}")
	public ResponseEntity<OrderResponse> getOrder(@PathVariable UUID orderId, Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID patientId = principal.getUserId();

		return ResponseEntity.ok(orderService.getOrder(patientId, orderId));
	}

	// =========================================================
	// PHARMACY SIDE
	// =========================================================

	// 4. Get All Orders Of Logged-in Pharmacy
	@GetMapping("/pharmacy")
	public ResponseEntity<List<OrderResponse>> getPharmacyOrders(Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		return ResponseEntity.ok(orderService.getPharmacyOrders(userId));
	}

	// 5. Get Particular Order - Pharmacy
	@GetMapping("/pharmacy/{orderId}")
	public ResponseEntity<OrderResponse> getPharmacyOrder(@PathVariable UUID orderId, Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		return ResponseEntity.ok(orderService.getPharmacyOrder(userId, orderId));
	}

	// 6. Update Order Status - Pharmacy
	@PatchMapping("/pharmacy/{orderId}/status")
	public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable UUID orderId, @RequestParam OrderStatus status,
			Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		return ResponseEntity.ok(orderService.updateOrderStatus(userId, orderId, status));
	}
}