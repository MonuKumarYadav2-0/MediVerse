package com.mediverse.consultationservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.mediverse.consultationservice.entity.ConsultationStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConsultationResponse {

    private UUID id;

    private UUID appointmentId;

    private UUID patientId;

    private UUID doctorId;

    private LocalDateTime scheduledAt;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    private ConsultationStatus status;

    private String meetingRoomId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}