package com.mediverse.paymentservice.service;

import java.util.List;
import java.util.UUID;

import com.mediverse.paymentservice.dto.PaymentRequest;
import com.mediverse.paymentservice.dto.PaymentResponse;
import com.mediverse.paymentservice.entity.PaymentStatus;

public interface PaymentService {

    PaymentResponse createPayment(
            UUID patientId,
            PaymentRequest request
    );

    PaymentResponse getPayment(
            UUID patientId,
            UUID paymentId
    );

    List<PaymentResponse> getMyPayments(
            UUID patientId
    );

    PaymentResponse updatePaymentStatus(
            UUID paymentId,
            PaymentStatus status
    );
}