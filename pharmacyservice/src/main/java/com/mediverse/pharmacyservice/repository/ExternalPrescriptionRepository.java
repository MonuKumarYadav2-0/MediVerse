package com.mediverse.pharmacyservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mediverse.pharmacyservice.entity.ExternalPrescription;
import com.mediverse.pharmacyservice.entity.PrescriptionStatus;

public interface ExternalPrescriptionRepository
        extends JpaRepository<ExternalPrescription, UUID> {

    List<ExternalPrescription> findByPatientId(UUID patientId);

    List<ExternalPrescription> findByStatus(
            PrescriptionStatus status
    );

    Optional<ExternalPrescription> findByIdAndPatientId(
            UUID prescriptionId,
            UUID patientId
    );
}