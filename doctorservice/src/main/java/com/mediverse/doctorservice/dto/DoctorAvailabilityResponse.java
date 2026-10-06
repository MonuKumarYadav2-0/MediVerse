package com.mediverse.doctorservice.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DoctorAvailabilityResponse {

    private UUID id;

    private UUID doctorId;

    private DayOfWeek dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;

    private Boolean available;
}