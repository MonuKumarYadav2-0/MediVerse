package com.mediverse.pharmacyservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderResponse {

    private UUID id;

    private UUID patientId;

    private UUID pharmacyId;

    private UUID prescriptionId;

    private OrderSourceType sourceType;

    private String status;

    private BigDecimal totalAmount;

    private LocalDateTime createdAt;

    private List<OrderItemResponse> items;
}