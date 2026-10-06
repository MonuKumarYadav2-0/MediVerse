package com.mediverse.prescriptionservice.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.mediverse.prescriptionservice.dto.MedicineRequest;
import com.mediverse.prescriptionservice.dto.MedicineResponse;
import com.mediverse.prescriptionservice.dto.PrescriptionResponse;
import com.mediverse.prescriptionservice.entity.Prescription;
import com.mediverse.prescriptionservice.entity.PrescriptionMedicine;

@Component
public class PrescriptionMapper {

    public PrescriptionMedicine toMedicineEntity(
            MedicineRequest request,
            java.util.UUID prescriptionId) {

        return PrescriptionMedicine.builder()
                .prescriptionId(prescriptionId)
                .medicineName(request.getMedicineName())
                .dosage(request.getDosage())
                .frequency(request.getFrequency())
                .duration(request.getDuration())
                .instructions(request.getInstructions())
                .build();
    }

    public MedicineResponse toMedicineResponse(
            PrescriptionMedicine medicine) {

        return MedicineResponse.builder()
                .id(medicine.getId())
                .medicineName(medicine.getMedicineName())
                .dosage(medicine.getDosage())
                .frequency(medicine.getFrequency())
                .duration(medicine.getDuration())
                .instructions(medicine.getInstructions())
                .build();
    }

    public PrescriptionResponse toResponse(
            Prescription prescription,
            List<PrescriptionMedicine> medicines) {

        return PrescriptionResponse.builder()
                .id(prescription.getId())
                .consultationId(prescription.getConsultationId())
                .patientId(prescription.getPatientId())
                .doctorId(prescription.getDoctorId())
                .diagnosis(prescription.getDiagnosis())
                .notes(prescription.getNotes())
                .createdAt(prescription.getCreatedAt())
                .medicines(
                        medicines.stream()
                                .map(this::toMedicineResponse)
                                .toList()
                )
                .build();
    }
}