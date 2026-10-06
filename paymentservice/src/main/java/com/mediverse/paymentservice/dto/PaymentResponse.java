package com.mediverse.paymentservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentResponse {

    private UUID id;

    private UUID patientId;

    private UUID referenceId;

    private String referenceType;

    private BigDecimal amount;

    private String paymentMethod;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}