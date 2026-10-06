package com.mediverse.laboratoryservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mediverse.laboratoryservice.entity.LabTest;

public interface LabTestRepository
        extends JpaRepository<LabTest, UUID> {

    List<LabTest> findByLaboratoryId(UUID laboratoryId);

    List<LabTest> findByLaboratoryIdAndActiveTrue(
            UUID laboratoryId
    );

    List<LabTest> findByTestNameContainingIgnoreCaseAndActiveTrue(
            String testName
    );
}