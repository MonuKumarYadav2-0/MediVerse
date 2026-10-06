package com.mediverse.laboratoryservice.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabTestRequest {

    @NotBlank
    private String testName;

    @NotBlank
    private String description;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal price;
}