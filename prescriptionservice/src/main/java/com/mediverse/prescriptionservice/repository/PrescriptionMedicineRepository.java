package com.mediverse.prescriptionservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mediverse.prescriptionservice.entity.PrescriptionMedicine;

public interface PrescriptionMedicineRepository
        extends JpaRepository<PrescriptionMedicine, UUID> {

    List<PrescriptionMedicine> findByPrescriptionId(UUID prescriptionId);
}