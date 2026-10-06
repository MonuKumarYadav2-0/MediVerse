package com.mediverse.laboratoryservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabReportRequest {

    @NotBlank
    private String reportUrl;

    private String remarks;
}