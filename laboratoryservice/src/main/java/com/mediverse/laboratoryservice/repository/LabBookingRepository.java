package com.mediverse.laboratoryservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mediverse.laboratoryservice.entity.LabBooking;
import com.mediverse.laboratoryservice.entity.BookingStatus;

public interface LabBookingRepository
        extends JpaRepository<LabBooking, UUID> {

    List<LabBooking> findByPatientId(UUID patientId);

    List<LabBooking> findByLaboratoryId(UUID laboratoryId);

    List<LabBooking> findByLaboratoryIdAndStatus(
            UUID laboratoryId,
            BookingStatus status
    );
}