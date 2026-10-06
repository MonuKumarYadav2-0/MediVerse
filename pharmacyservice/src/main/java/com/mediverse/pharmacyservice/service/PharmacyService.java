package com.mediverse.pharmacyservice.service;

import java.util.UUID;

import com.mediverse.pharmacyservice.dto.PharmacyRequest;
import com.mediverse.pharmacyservice.dto.PharmacyResponse;

public interface PharmacyService {

    PharmacyResponse createPharmacy(
            UUID userId,
            PharmacyRequest request
    );

    PharmacyResponse getMyPharmacy(UUID userId);

    PharmacyResponse updateMyPharmacy(
            UUID userId,
            PharmacyRequest request
    );
}