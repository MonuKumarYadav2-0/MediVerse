package com.mediverse.doctorservice.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class DoctorRequest {

    @NotBlank
    private String fullName;

    @NotBlank
    private String specialization;

    @NotBlank
    private String qualification;

    @NotNull
    @Min(0)
    private Integer experienceYears;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal consultationFee;

    @NotBlank
    private String registrationNumber;

    @Size(max = 1000)
    private String bio;

    private String clinicAddress;

    private String city;

    private String state;

    @NotNull
    private Boolean availableForOnlineConsultation;
}