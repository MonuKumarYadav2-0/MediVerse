package com.mediverse.pharmacyservice.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MedicineResponse {

    private UUID id;

    private UUID pharmacyId;

    private String pharmacyName;

    private String medicineName;

    private String description;

    private BigDecimal price;

    private Integer availableQuantity;

    private Boolean active;
}