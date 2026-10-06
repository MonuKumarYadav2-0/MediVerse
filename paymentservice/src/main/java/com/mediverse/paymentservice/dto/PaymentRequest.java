package com.mediverse.paymentservice.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.mediverse.paymentservice.entity.PaymentMethod;
import com.mediverse.paymentservice.entity.PaymentReferenceType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {

    @NotNull
    private UUID referenceId;

    @NotNull
    private PaymentReferenceType referenceType;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    @NotNull
    private PaymentMethod paymentMethod;
}