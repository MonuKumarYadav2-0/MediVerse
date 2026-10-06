package com.mediverse.laboratoryservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mediverse.laboratoryservice.entity.Laboratory;

public interface LaboratoryRepository
        extends JpaRepository<Laboratory, UUID> {

    Optional<Laboratory> findByUserId(UUID userId);

    boolean existsByUserId(UUID userId);
}