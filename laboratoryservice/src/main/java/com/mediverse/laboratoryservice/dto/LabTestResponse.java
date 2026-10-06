package com.mediverse.laboratoryservice.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LabTestResponse {

    private UUID id;

    private UUID laboratoryId;

    private String testName;

    private String description;

    private BigDecimal price;

    private Boolean active;
}