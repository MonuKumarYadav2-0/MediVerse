package com.mediverse.prescriptionservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mediverse.prescriptionservice.entity.Prescription;

public interface PrescriptionRepository
        extends JpaRepository<Prescription, UUID> {

    List<Prescription> findByPatientId(UUID patientId);

    List<Prescription> findByDoctorId(UUID doctorId);

    boolean existsByConsultationId(UUID consultationId);
}