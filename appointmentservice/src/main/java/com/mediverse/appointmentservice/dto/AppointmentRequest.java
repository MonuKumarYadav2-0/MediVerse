package com.mediverse.appointmentservice.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import com.mediverse.appointmentservice.entity.AppointmentType;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentRequest {

    @NotNull
    private UUID doctorId;

    @NotNull
    @FutureOrPresent
    private LocalDate appointmentDate;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @NotNull
    private AppointmentType type;

    @Size(max = 1000)
    private String reason;
}