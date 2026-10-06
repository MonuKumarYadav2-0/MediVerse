package com.mediverse.prescriptionservice.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PrescriptionResponse {

    private UUID id;

    private UUID consultationId;

    private UUID patientId;

    private UUID doctorId;

    private String diagnosis;

    private String notes;

    private List<MedicineResponse> medicines;

    private LocalDateTime createdAt;
}