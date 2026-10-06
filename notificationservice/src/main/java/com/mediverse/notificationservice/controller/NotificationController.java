package com.mediverse.notificationservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.mediverse.notificationservice.dto.NotificationRequest;
import com.mediverse.notificationservice.dto.NotificationResponse;
import com.mediverse.notificationservice.security.UserPrincipal;
import com.mediverse.notificationservice.service.NotificationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	// ==========================================
	// CREATE NOTIFICATION
	// ==========================================

	@PostMapping
	public ResponseEntity<NotificationResponse> createNotification(@Valid @RequestBody NotificationRequest request) {

		return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.createNotification(request));
	}

	// ==========================================
	// MY NOTIFICATIONS
	// ==========================================

	@GetMapping("/me")
	public ResponseEntity<List<NotificationResponse>> getMyNotifications(Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		return ResponseEntity.ok(notificationService.getMyNotifications(userId));
	}

	// ==========================================
	// UNREAD NOTIFICATIONS
	// ==========================================

	@GetMapping("/me/unread")
	public ResponseEntity<List<NotificationResponse>> getUnreadNotifications(Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		return ResponseEntity.ok(notificationService.getUnreadNotifications(userId));
	}

	// ==========================================
	// UNREAD COUNT
	// ==========================================

	@GetMapping("/me/unread/count")
	public ResponseEntity<Long> getUnreadCount(Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		return ResponseEntity.ok(notificationService.getUnreadCount(userId));
	}

	// ==========================================
	// MARK AS READ
	// ==========================================

	@PatchMapping("/{notificationId}/read")
	public ResponseEntity<NotificationResponse> markAsRead(@PathVariable UUID notificationId,
			Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		return ResponseEntity.ok(notificationService.markAsRead(userId, notificationId));
	}

	// ==========================================
	// DELETE
	// ==========================================

	@DeleteMapping("/{notificationId}")
	public ResponseEntity<Void> deleteNotification(@PathVariable UUID notificationId, Authentication authentication) {

		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

		UUID userId = principal.getUserId();

		notificationService.deleteNotification(userId, notificationId);

		return ResponseEntity.noContent().build();
	}
}