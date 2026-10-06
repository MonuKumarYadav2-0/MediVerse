package com.mediverse.pharmacyservice.dto;

import java.util.UUID;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderRequest {

    @NotNull
    private UUID pharmacyId;

    private UUID prescriptionId;

    @NotNull
    private OrderSourceType sourceType;

    @NotNull
    @Valid
    private List<OrderItemRequest> items;
}