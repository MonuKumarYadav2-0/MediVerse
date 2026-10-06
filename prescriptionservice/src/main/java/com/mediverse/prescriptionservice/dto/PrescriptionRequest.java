package com.mediverse.prescriptionservice.dto;

import java.util.UUID;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrescriptionRequest {

    @NotNull
    private UUID consultationId;

    @NotNull
    private UUID patientId;

    @Size(max = 1000)
    private String diagnosis;

    @Size(max = 2000)
    private String notes;

    @NotNull
    @Size(min = 1)
    @Valid
    private List<MedicineRequest> medicines;
}