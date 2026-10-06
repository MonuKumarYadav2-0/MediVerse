package com.mediverse.notificationservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationResponse {

    private UUID id;

    private UUID userId;

    private String title;

    private String message;

    private String type;

    private String status;

    private UUID referenceId;

    private LocalDateTime createdAt;
}