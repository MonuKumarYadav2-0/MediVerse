package com.mediverse.appointmentservice.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorResponse {

    private UUID id;
    private UUID userId;
    private String fullName;
    private BigDecimal consultationFee;
}