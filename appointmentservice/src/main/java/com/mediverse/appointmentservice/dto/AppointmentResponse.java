package com.mediverse.appointmentservice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import com.mediverse.appointmentservice.entity.AppointmentStatus;
import com.mediverse.appointmentservice.entity.AppointmentType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AppointmentResponse {

    private UUID id;

    private UUID patientId;

    private UUID doctorId;

    private LocalDate appointmentDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private AppointmentType type;

    private AppointmentStatus status;

    private String reason;

    private BigDecimal consultationFee;
}