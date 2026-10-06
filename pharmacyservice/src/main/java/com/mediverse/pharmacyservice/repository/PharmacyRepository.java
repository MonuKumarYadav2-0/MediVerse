package com.mediverse.pharmacyservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mediverse.pharmacyservice.entity.Pharmacy;

public interface PharmacyRepository
        extends JpaRepository<Pharmacy, UUID> {

    Optional<Pharmacy> findByUserId(UUID userId);

    boolean existsByUserId(UUID userId);
}