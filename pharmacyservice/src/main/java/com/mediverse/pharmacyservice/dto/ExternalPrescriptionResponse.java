package com.mediverse.pharmacyservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.mediverse.pharmacyservice.entity.PrescriptionStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExternalPrescriptionResponse {

    private UUID id;

    private UUID patientId;

    private String fileName;

    private String contentType;

    private PrescriptionStatus status;

    private LocalDateTime uploadedAt;
}