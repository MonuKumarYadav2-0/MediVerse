package com.mediverse.laboratoryservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LabReportResponse {

    private UUID id;

    private UUID bookingId;

    private String reportUrl;

    private String remarks;

    private LocalDateTime generatedAt;
}