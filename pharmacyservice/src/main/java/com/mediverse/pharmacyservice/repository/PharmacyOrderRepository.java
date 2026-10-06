package com.mediverse.pharmacyservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mediverse.pharmacyservice.entity.PharmacyOrder;
import com.mediverse.pharmacyservice.entity.OrderStatus;

public interface PharmacyOrderRepository
        extends JpaRepository<PharmacyOrder, UUID> {

    List<PharmacyOrder> findByPatientId(UUID patientId);

    List<PharmacyOrder> findByPharmacyId(UUID pharmacyId);

    List<PharmacyOrder> findByPharmacyIdAndStatus(
            UUID pharmacyId,
            OrderStatus status
    );
    
    
}