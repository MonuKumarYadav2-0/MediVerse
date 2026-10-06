package com.mediverse.doctorservice.repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mediverse.doctorservice.entity.DoctorAvailability;

public interface DoctorAvailabilityRepository
        extends JpaRepository<DoctorAvailability, UUID> {

    List<DoctorAvailability> findByDoctorId(UUID doctorId);

    Optional<DoctorAvailability> findByDoctorIdAndDayOfWeek(
            UUID doctorId,
            DayOfWeek dayOfWeek
    );

    boolean existsByDoctorIdAndDayOfWeek(
            UUID doctorId,
            DayOfWeek dayOfWeek
    );
}