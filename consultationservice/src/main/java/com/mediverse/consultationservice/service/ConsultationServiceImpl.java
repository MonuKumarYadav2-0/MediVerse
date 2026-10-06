package com.mediverse.consultationservice.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mediverse.consultationservice.client.AppointmentServiceClient;
import com.mediverse.consultationservice.dto.AppointmentResponse;
import com.mediverse.consultationservice.dto.ConsultationRequest;
import com.mediverse.consultationservice.dto.ConsultationResponse;
import com.mediverse.consultationservice.entity.Consultation;
import com.mediverse.consultationservice.entity.ConsultationStatus;
import com.mediverse.consultationservice.mapper.ConsultationMapper;
import com.mediverse.consultationservice.repository.ConsultationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsultationServiceImpl implements ConsultationService {

	private final ConsultationRepository consultationRepository;
	private final ConsultationMapper consultationMapper;
	private final AppointmentServiceClient appointmentServiceClient;

	@Override
	@Transactional
	public ConsultationResponse createConsultation(UUID userId, String token, ConsultationRequest request) {

		// 1. Check duplicate consultation
		if (consultationRepository.existsByAppointmentId(request.getAppointmentId())) {

			throw new RuntimeException("Consultation already exists for this appointment");
		}

		// 2. Get appointment from Appointment Service
		AppointmentResponse appointment = appointmentServiceClient.getAppointment(request.getAppointmentId(), token);

		// 3. Appointment must be CONFIRMED
		if (!"CONFIRMED".equals(appointment.getStatus())) {

			throw new RuntimeException("Consultation can only be created for a confirmed appointment");
		}

		// 4. Verify patient or doctor belongs to appointment
		if (!appointment.getPatientId().equals(userId) && !appointment.getDoctorId().equals(userId)) {

			throw new RuntimeException("You are not part of this appointment");
		}

		// 5. Create scheduled date and time
		LocalDateTime scheduledAt = LocalDateTime.of(appointment.getAppointmentDate(), appointment.getStartTime());

		// 6. Create consultation
		Consultation consultation = Consultation.builder().appointmentId(appointment.getId())
				.patientId(appointment.getPatientId()).doctorId(appointment.getDoctorId()).scheduledAt(scheduledAt)
				.status(ConsultationStatus.SCHEDULED).build();

		// 7. Save
		Consultation saved = consultationRepository.save(consultation);

		// 8. Return response
		return consultationMapper.toResponse(saved);
	}

	@Override
	@Transactional(readOnly = true)
	public ConsultationResponse getConsultation(UUID userId, UUID consultationId) {

		Consultation consultation = consultationRepository.findById(consultationId)
				.orElseThrow(() -> new RuntimeException("Consultation not found"));

		verifyParticipant(consultation, userId);

		return consultationMapper.toResponse(consultation);
	}

	@Override
	@Transactional
	public ConsultationResponse startConsultation(UUID userId, UUID consultationId) {

		Consultation consultation = consultationRepository.findById(consultationId)
				.orElseThrow(() -> new RuntimeException("Consultation not found"));

		verifyParticipant(consultation, userId);

		if (consultation.getStatus() != ConsultationStatus.SCHEDULED) {

			throw new RuntimeException("Only scheduled consultations can be started");
		}

		consultation.setStatus(ConsultationStatus.IN_PROGRESS);

		consultation.setStartedAt(LocalDateTime.now());

		Consultation updated = consultationRepository.save(consultation);

		return consultationMapper.toResponse(updated);
	}

	@Override
	@Transactional
	public ConsultationResponse endConsultation(UUID userId, UUID consultationId) {

		Consultation consultation = consultationRepository.findById(consultationId)
				.orElseThrow(() -> new RuntimeException("Consultation not found"));

		verifyParticipant(consultation, userId);

		if (consultation.getStatus() != ConsultationStatus.IN_PROGRESS) {

			throw new RuntimeException("Only ongoing consultations can be ended");
		}

		consultation.setStatus(ConsultationStatus.COMPLETED);

		consultation.setEndedAt(LocalDateTime.now());

		Consultation updated = consultationRepository.save(consultation);

		return consultationMapper.toResponse(updated);
	}

	private void verifyParticipant(Consultation consultation, UUID userId) {

		if (!consultation.getPatientId().equals(userId) && !consultation.getDoctorId().equals(userId)) {

			throw new RuntimeException("You are not part of this consultation");
		}
	}
}