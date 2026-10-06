package com.mediverse.notificationservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mediverse.notificationservice.dto.NotificationRequest;
import com.mediverse.notificationservice.dto.NotificationResponse;
import com.mediverse.notificationservice.entity.Notification;
import com.mediverse.notificationservice.entity.NotificationStatus;
import com.mediverse.notificationservice.messaging.NotificationMessage;
import com.mediverse.notificationservice.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl
        implements NotificationService {

    private final NotificationRepository notificationRepository;

    // ==========================================
    // CREATE NOTIFICATION
    // ==========================================

    @Override
    public NotificationResponse createNotification(
            NotificationRequest request) {

        Notification notification =
                Notification.builder()
                        .userId(request.getUserId())
                        .title(request.getTitle())
                        .message(request.getMessage())
                        .type(request.getType())
                        .status(NotificationStatus.UNREAD)
                        .referenceId(request.getReferenceId())
                        .createdAt(LocalDateTime.now())
                        .build();

        return mapToResponse(
                notificationRepository.save(notification)
        );
    }

    // ==========================================
    // MY NOTIFICATIONS
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getMyNotifications(
            UUID userId) {

        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ==========================================
    // UNREAD NOTIFICATIONS
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getUnreadNotifications(
            UUID userId) {

        return notificationRepository
                .findByUserIdAndStatusOrderByCreatedAtDesc(
                        userId,
                        NotificationStatus.UNREAD
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ==========================================
    // UNREAD COUNT
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public long getUnreadCount(UUID userId) {

        return notificationRepository
                .countByUserIdAndStatus(
                        userId,
                        NotificationStatus.UNREAD
                );
    }

    // ==========================================
    // MARK AS READ
    // ==========================================

    @Override
    public NotificationResponse markAsRead(
            UUID userId,
            UUID notificationId) {

        Notification notification =
                notificationRepository
                        .findById(notificationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification not found"
                                )
                        );

        if (!notification.getUserId()
                .equals(userId)) {

            throw new RuntimeException(
                    "You cannot access this notification"
            );
        }

        notification.setStatus(
                NotificationStatus.READ
        );

        return mapToResponse(notification);
    }

    // ==========================================
    // DELETE
    // ==========================================

    @Override
    public void deleteNotification(
            UUID userId,
            UUID notificationId) {

        Notification notification =
                notificationRepository
                        .findById(notificationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification not found"
                                )
                        );

        if (!notification.getUserId()
                .equals(userId)) {

            throw new RuntimeException(
                    "You cannot delete this notification"
            );
        }

        notificationRepository.delete(notification);
    }

    
    @Override
    public void processNotificationEvent(
            NotificationMessage message) {

        Notification notification =
                Notification.builder()
                        .userId(message.getUserId())
                        .title(message.getTitle())
                        .message(message.getMessage())
                        .type(message.getType())
                        .status(NotificationStatus.UNREAD)
                        .referenceId(message.getReferenceId())
                        .createdAt(LocalDateTime.now())
                        .build();

        notificationRepository.save(notification);
    }
    
    
    // ==========================================
    // MAPPING
    // ==========================================

    private NotificationResponse mapToResponse(
            Notification notification) {

        return NotificationResponse.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType().name())
                .status(notification.getStatus().name())
                .referenceId(
                        notification.getReferenceId()
                )
                .createdAt(
                        notification.getCreatedAt()
                )
                .build();
    }
}