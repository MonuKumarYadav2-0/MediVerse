package com.mediverse.notificationservice.service;

import java.util.List;
import java.util.UUID;

import com.mediverse.notificationservice.dto.NotificationRequest;
import com.mediverse.notificationservice.dto.NotificationResponse;
import com.mediverse.notificationservice.messaging.NotificationMessage;

public interface NotificationService {

	NotificationResponse createNotification(NotificationRequest request);

	List<NotificationResponse> getMyNotifications(UUID userId);

	List<NotificationResponse> getUnreadNotifications(UUID userId);

	long getUnreadCount(UUID userId);

	NotificationResponse markAsRead(UUID userId, UUID notificationId);

	void deleteNotification(UUID userId, UUID notificationId);

	void processNotificationEvent(NotificationMessage message);
}