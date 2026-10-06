package com.mediverse.notificationservice.messaging;

import java.util.UUID;

import com.mediverse.notificationservice.entity.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage {

    private UUID userId;

    private String title;

    private String message;

    private NotificationType type;

    private UUID referenceId;
}