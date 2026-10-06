package com.mediverse.pharmacyservice.dto;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequest {

    @NotNull
    private UUID medicineId;

    @NotNull
    @Min(1)
    private Integer quantity;
}