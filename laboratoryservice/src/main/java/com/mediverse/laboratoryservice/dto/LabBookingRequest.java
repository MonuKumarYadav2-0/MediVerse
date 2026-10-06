package com.mediverse.laboratoryservice.dto;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabBookingRequest {

    @NotNull
    private UUID laboratoryId;

    @NotNull
    private UUID testId;

    @NotNull
    @FutureOrPresent
    private LocalDate bookingDate;
}