package com.mediverse.consultationservice.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsultationRequest {

    @NotNull
    private UUID appointmentId;
}