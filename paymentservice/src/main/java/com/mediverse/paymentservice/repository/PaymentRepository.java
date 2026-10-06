package com.mediverse.paymentservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mediverse.paymentservice.entity.Payment;
import com.mediverse.paymentservice.entity.PaymentStatus;

public interface PaymentRepository
        extends JpaRepository<Payment, UUID> {

    List<Payment> findByPatientId(UUID patientId);

    List<Payment> findByPatientIdAndStatus(
            UUID patientId,
            PaymentStatus status
    );

    List<Payment> findByReferenceId(UUID referenceId);

    boolean existsByReferenceIdAndReferenceType(
            UUID referenceId,
            com.mediverse.paymentservice.entity.PaymentReferenceType referenceType
    );
}