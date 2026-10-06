package com.mediverse.doctorservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mediverse.doctorservice.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    Optional<Doctor> findByUserId(UUID userId);

    boolean existsByUserId(UUID userId);

    boolean existsByRegistrationNumber(String registrationNumber);
}