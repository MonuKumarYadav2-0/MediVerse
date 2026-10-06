package com.mediverse.laboratoryservice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LabBookingResponse {

    private UUID id;

    private UUID patientId;

    private UUID laboratoryId;

    private UUID testId;

    private LocalDate bookingDate;

    private String status;

    private BigDecimal amount;

    private LocalDateTime createdAt;
}