package com.mediverse.pharmacyservice.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderItemResponse {

    private UUID id;

    private UUID medicineId;

    private String medicineName;

    private Integer quantity;

    private BigDecimal price;

    private BigDecimal subtotal;
}