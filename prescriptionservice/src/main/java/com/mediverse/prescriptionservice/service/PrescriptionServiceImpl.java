package com.mediverse.prescriptionservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mediverse.prescriptionservice.client.ConsultationServiceClient;
import com.mediverse.prescriptionservice.dto.ConsultationResponse;
import com.mediverse.prescriptionservice.dto.PrescriptionRequest;
import com.mediverse.prescriptionservice.dto.PrescriptionResponse;
import com.mediverse.prescriptionservice.entity.Prescription;
import com.mediverse.prescriptionservice.entity.PrescriptionMedicine;
import com.mediverse.prescriptionservice.mapper.PrescriptionMapper;
import com.mediverse.prescriptionservice.repository.PrescriptionMedicineRepository;
import com.mediverse.prescriptionservice.repository.PrescriptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionMedicineRepository medicineRepository;
    private final PrescriptionMapper prescriptionMapper;
    private final ConsultationServiceClient consultationServiceClient;

	@Override
	@Transactional
	public PrescriptionResponse createPrescription(UUID doctorId, PrescriptionRequest request, String token) {

		// 1. Get consultation from Consultation Service
		ConsultationResponse consultation = consultationServiceClient.getConsultation(request.getConsultationId(),
				token);

		// 2. Check consultation exists
		if (consultation == null) {
			throw new RuntimeException("Consultation not found");
		}

		// 3. Consultation must be completed
		if (consultation.getStatus() == null || !"COMPLETED".equals(consultation.getStatus().name())) {

			throw new RuntimeException("Prescription can only be created for completed consultation");
		}

		// 4. Verify doctor
		if (consultation.getDoctorId() == null || !consultation.getDoctorId().equals(doctorId)) {

			throw new RuntimeException("Doctor does not belong to this consultation");
		}

		// 5. Verify patient
		if (consultation.getPatientId() == null || !consultation.getPatientId().equals(request.getPatientId())) {

			throw new RuntimeException("Patient does not belong to this consultation");
		}

		// 6. Prevent duplicate prescription
		if (prescriptionRepository.existsByConsultationId(request.getConsultationId())) {

			throw new RuntimeException("Prescription already exists for this consultation");
		}

		// 7. Create prescription
		Prescription prescription = Prescription.builder().consultationId(request.getConsultationId())
				.patientId(request.getPatientId()).doctorId(doctorId).diagnosis(request.getDiagnosis())
				.notes(request.getNotes()).build();

		Prescription savedPrescription = prescriptionRepository.save(prescription);

		// 8. Create medicines
		List<PrescriptionMedicine> medicines = request.getMedicines().stream()
				.map(medicine -> prescriptionMapper.toMedicineEntity(medicine, savedPrescription.getId())).toList();

		// 9. Save medicines
		List<PrescriptionMedicine> savedMedicines = medicineRepository.saveAll(medicines);

		// 10. Return response
		return prescriptionMapper.toResponse(savedPrescription, savedMedicines);
	}

    @Override
    @Transactional(readOnly = true)
    public List<PrescriptionResponse> getMyPrescriptions(
            UUID patientId) {

        return prescriptionRepository
                .findByPatientId(patientId)
                .stream()
                .map(prescription -> {

                    List<PrescriptionMedicine> medicines =
                            medicineRepository
                                    .findByPrescriptionId(
                                            prescription.getId()
                                    );

                    return prescriptionMapper.toResponse(
                            prescription,
                            medicines
                    );
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrescriptionResponse> getDoctorPrescriptions(
            UUID doctorId) {

        return prescriptionRepository
                .findByDoctorId(doctorId)
                .stream()
                .map(prescription -> {

                    List<PrescriptionMedicine> medicines =
                            medicineRepository
                                    .findByPrescriptionId(
                                            prescription.getId()
                                    );

                    return prescriptionMapper.toResponse(
                            prescription,
                            medicines
                    );
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PrescriptionResponse getPrescriptionById(
            UUID prescriptionId) {

        Prescription prescription =
                prescriptionRepository
                        .findById(prescriptionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Prescription not found"
                                )
                        );

        List<PrescriptionMedicine> medicines =
                medicineRepository
                        .findByPrescriptionId(
                                prescriptionId
                        );

        return prescriptionMapper.toResponse(
                prescription,
                medicines
        );
    }
}