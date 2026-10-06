package com.mediverse.doctorservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorResponse {

    private UUID id;

    private UUID userId;

    private String fullName;

    private String specialization;

    private String qualification;

    private Integer experienceYears;

    private BigDecimal consultationFee;

    private String registrationNumber;

    private String bio;

    private String clinicAddress;

    private String city;

    private String state;

    private Boolean availableForOnlineConsultation;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}