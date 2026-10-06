package com.mediverse.prescriptionservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
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