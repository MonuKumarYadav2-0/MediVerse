package com.mediverse.laboratoryservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mediverse.laboratoryservice.entity.LabReport;

public interface LabReportRepository
        extends JpaRepository<LabReport, UUID> {

    Optional<LabReport> findByBookingId(UUID bookingId);
}