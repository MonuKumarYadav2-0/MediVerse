package com.mediverse.notificationservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediverse.notificationservice.messaging.NotificationMessage;
import com.mediverse.notificationservice.messaging.NotificationProducer;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/test/notifications")
@RequiredArgsConstructor
public class NotificationTestController {

	private final NotificationProducer notificationProducer;

	@PostMapping
	public ResponseEntity<String> sendTestNotification(@RequestBody NotificationMessage message) {

		notificationProducer.sendNotification(message);

		return ResponseEntity.ok("Notification event sent to RabbitMQ");
	}
}