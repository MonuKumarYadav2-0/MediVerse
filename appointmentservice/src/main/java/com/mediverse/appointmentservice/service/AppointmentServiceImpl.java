package com.mediverse.appointmentservice.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mediverse.appointmentservice.client.DoctorServiceClient;
import com.mediverse.appointmentservice.dto.AppointmentRequest;
import com.mediverse.appointmentservice.dto.AppointmentResponse;
import com.mediverse.appointmentservice.dto.DoctorAvailabilityResponse;
import com.mediverse.appointmentservice.dto.DoctorResponse;
import com.mediverse.appointmentservice.entity.Appointment;
import com.mediverse.appointmentservice.entity.AppointmentStatus;
import com.mediverse.appointmentservice.mapper.AppointmentMapper;
import com.mediverse.appointmentservice.repository.AppointmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

	private final AppointmentRepository appointmentRepository;
	private final AppointmentMapper appointmentMapper;
	private final DoctorServiceClient doctorServiceClient;

	@Override
	@Transactional
	public AppointmentResponse createAppointment(UUID patientId, AppointmentRequest request) {

		// 1. Validate time
		if (!request.getStartTime().isBefore(request.getEndTime())) {

			throw new RuntimeException("Start time must be before end time");
		}

		// 2. Check doctor's availability
		DoctorAvailabilityResponse availability = doctorServiceClient.getDoctorAvailability(request.getDoctorId(),
				request.getAppointmentDate().getDayOfWeek());

		if (availability.getAvailable() == null || !availability.getAvailable()) {

			throw new RuntimeException("Doctor is not available on this day");
		}

		// 3. Check requested time
		if (request.getStartTime().isBefore(availability.getStartTime())
				|| request.getEndTime().isAfter(availability.getEndTime())) {

			throw new RuntimeException("Requested time is outside doctor's availability");
		}

		// 4. Check double booking
		boolean alreadyBooked = appointmentRepository.existsByDoctorIdAndAppointmentDateAndStartTime(
				request.getDoctorId(), request.getAppointmentDate(), request.getStartTime());

		if (alreadyBooked) {

			throw new RuntimeException("Doctor is already booked for this slot");
		}

		// 5. Get doctor details
		DoctorResponse doctor = doctorServiceClient.getDoctor(request.getDoctorId());

		// 6. Get consultation fee from Doctor Service
		BigDecimal consultationFee = doctor.getConsultationFee();

		if (consultationFee == null) {

			throw new RuntimeException("Doctor consultation fee is not available");
		}

		// 7. Convert request → entity
		Appointment appointment = appointmentMapper.toEntity(request);

		// 8. Patient ID comes from JWT
		appointment.setPatientId(patientId);

		// 9. Set consultation fee from Doctor Service
		appointment.setConsultationFee(consultationFee);

		// 10. Initial status
		appointment.setStatus(AppointmentStatus.PENDING);

		// 11. Save
		Appointment saved = appointmentRepository.save(appointment);

		// 12. Convert entity → response
		return appointmentMapper.toResponse(saved);
	}

	@Override
	@Transactional(readOnly = true)
	public List<AppointmentResponse> getMyAppointments(UUID patientId) {

		return appointmentRepository.findByPatientId(patientId).stream().map(appointmentMapper::toResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public AppointmentResponse getAppointmentById(UUID appointmentId) {

		Appointment appointment = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new RuntimeException("Appointment not found"));

		return appointmentMapper.toResponse(appointment);
	}

	@Override
	@Transactional
	public void cancelAppointment(UUID patientId, UUID appointmentId) {

		Appointment appointment = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new RuntimeException("Appointment not found"));

		if (!appointment.getPatientId().equals(patientId)) {

			throw new RuntimeException("You cannot cancel this appointment");
		}

		if (appointment.getStatus() == AppointmentStatus.COMPLETED) {

			throw new RuntimeException("Completed appointment cannot be cancelled");
		}

		appointment.setStatus(AppointmentStatus.CANCELLED);

		appointmentRepository.save(appointment);
	}

	@Override
	@Transactional(readOnly = true)
	public List<AppointmentResponse> getDoctorAppointments(UUID doctorId) {

		return appointmentRepository.findByDoctorId(doctorId).stream().map(appointmentMapper::toResponse).toList();
	}

	@Override
	@Transactional
	public AppointmentResponse confirmAppointment(UUID doctorId, UUID appointmentId) {

		Appointment appointment = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new RuntimeException("Appointment not found"));

		if (!appointment.getDoctorId().equals(doctorId)) {

			throw new RuntimeException("You cannot confirm this appointment");
		}

		if (appointment.getStatus() != AppointmentStatus.PENDING) {

			throw new RuntimeException("Only pending appointments can be confirmed");
		}

		appointment.setStatus(AppointmentStatus.CONFIRMED);

		Appointment updated = appointmentRepository.save(appointment);

		return appointmentMapper.toResponse(updated);
	}

	@Override
	@Transactional
	public AppointmentResponse completeAppointment(UUID doctorId, UUID appointmentId) {

		Appointment appointment = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new RuntimeException("Appointment not found"));

		if (!appointment.getDoctorId().equals(doctorId)) {

			throw new RuntimeException("You cannot complete this appointment");
		}

		if (appointment.getStatus() != AppointmentStatus.CONFIRMED) {

			throw new RuntimeException("Only confirmed appointments can be completed");
		}

		appointment.setStatus(AppointmentStatus.COMPLETED);

		Appointment updated = appointmentRepository.save(appointment);

		return appointmentMapper.toResponse(updated);
	}
}