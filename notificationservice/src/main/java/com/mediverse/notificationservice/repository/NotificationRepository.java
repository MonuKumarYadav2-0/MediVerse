package com.mediverse.notificationservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mediverse.notificationservice.entity.Notification;
import com.mediverse.notificationservice.entity.NotificationStatus;

public interface NotificationRepository
        extends JpaRepository<Notification, UUID> {

    List<Notification> findByUserIdOrderByCreatedAtDesc(
            UUID userId
    );

    List<Notification> findByUserIdAndStatusOrderByCreatedAtDesc(
            UUID userId,
            NotificationStatus status
    );

    long countByUserIdAndStatus(
            UUID userId,
            NotificationStatus status
    );
}