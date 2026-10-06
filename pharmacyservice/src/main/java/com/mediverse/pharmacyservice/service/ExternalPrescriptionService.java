package com.mediverse.pharmacyservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.mediverse.pharmacyservice.dto.ExternalPrescriptionResponse;

public interface ExternalPrescriptionService {

    ExternalPrescriptionResponse uploadPrescription(
            UUID patientId,
            MultipartFile file
    );

    List<ExternalPrescriptionResponse> getMyPrescriptions(
            UUID patientId
    );
}