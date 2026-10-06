package com.mediverse.prescriptionservice.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MedicineResponse {

    private UUID id;

    private String medicineName;

    private String dosage;

    private String frequency;

    private String duration;

    private String instructions;
}