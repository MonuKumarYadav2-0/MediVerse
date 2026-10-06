package com.mediverse.consultationservice.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentResponse {

    private UUID id;

    private UUID patientId;

    private UUID doctorId;

    private LocalDate appointmentDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private AppointmentType type;

    private String status;

    private String reason;
}