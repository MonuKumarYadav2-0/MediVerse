package com.mediverse.pharmacyservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mediverse.pharmacyservice.entity.Medicine;

public interface MedicineRepository
        extends JpaRepository<Medicine, UUID> {

    List<Medicine> findByPharmacyId(UUID pharmacyId);

    List<Medicine> findByPharmacyIdAndActiveTrue(UUID pharmacyId);

    boolean existsByIdAndPharmacyId(
            UUID medicineId,
            UUID pharmacyId
    );
    
    @Query("""
            SELECT m
            FROM Medicine m
            WHERE LOWER(m.medicineName) LIKE LOWER(CONCAT('%', :name, '%'))
            AND m.active = true
            """)
    List<Medicine> searchActiveMedicines(
            @Param("name") String name
    );
}