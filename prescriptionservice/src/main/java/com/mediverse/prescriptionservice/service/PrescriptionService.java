package com.mediverse.prescriptionservice.service;

import java.util.List;
import java.util.UUID;

import com.mediverse.prescriptionservice.dto.PrescriptionRequest;
import com.mediverse.prescriptionservice.dto.PrescriptionResponse;

public interface PrescriptionService {


    List<PrescriptionResponse> getMyPrescriptions(UUID patientId);

    List<PrescriptionResponse> getDoctorPrescriptions(UUID doctorId);

    PrescriptionResponse getPrescriptionById(UUID prescriptionId);

	PrescriptionResponse createPrescription(UUID doctorId, PrescriptionRequest request, String token);
}