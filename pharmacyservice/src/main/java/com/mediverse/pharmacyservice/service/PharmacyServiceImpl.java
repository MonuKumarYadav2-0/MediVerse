package com.mediverse.pharmacyservice.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mediverse.pharmacyservice.dto.PharmacyRequest;
import com.mediverse.pharmacyservice.dto.PharmacyResponse;
import com.mediverse.pharmacyservice.entity.Pharmacy;
import com.mediverse.pharmacyservice.repository.PharmacyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PharmacyServiceImpl implements PharmacyService {

    private final PharmacyRepository pharmacyRepository;

    @Override
    public PharmacyResponse createPharmacy(
            UUID userId,
            PharmacyRequest request) {

        if (pharmacyRepository.existsByUserId(userId)) {
            throw new RuntimeException(
                    "Pharmacy profile already exists"
            );
        }

        Pharmacy pharmacy = Pharmacy.builder()
                .userId(userId)
                .pharmacyName(request.getPharmacyName())
                .ownerName(request.getOwnerName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .active(true)
                .build();

        Pharmacy saved =
                pharmacyRepository.save(pharmacy);

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PharmacyResponse getMyPharmacy(UUID userId) {

        Pharmacy pharmacy =
                pharmacyRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Pharmacy profile not found"
                                )
                        );

        return mapToResponse(pharmacy);
    }

    @Override
    public PharmacyResponse updateMyPharmacy(
            UUID userId,
            PharmacyRequest request) {

        Pharmacy pharmacy =
                pharmacyRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Pharmacy profile not found"
                                )
                        );

        pharmacy.setPharmacyName(
                request.getPharmacyName()
        );

        pharmacy.setOwnerName(
                request.getOwnerName()
        );

        pharmacy.setPhone(
                request.getPhone()
        );

        pharmacy.setAddress(
                request.getAddress()
        );

        return mapToResponse(pharmacy);
    }

    private PharmacyResponse mapToResponse(
            Pharmacy pharmacy) {

        return PharmacyResponse.builder()
                .id(pharmacy.getId())
                .userId(pharmacy.getUserId())
                .pharmacyName(
                        pharmacy.getPharmacyName()
                )
                .ownerName(
                        pharmacy.getOwnerName()
                )
                .phone(
                        pharmacy.getPhone()
                )
                .address(
                        pharmacy.getAddress()
                )
                .active(
                        pharmacy.getActive()
                )
                .build();
    }
}