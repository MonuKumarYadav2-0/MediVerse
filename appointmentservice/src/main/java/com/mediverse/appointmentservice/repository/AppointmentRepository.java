package com.mediverse.appointmentservice.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mediverse.appointmentservice.entity.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

	List<Appointment> findByPatientId(UUID patientId);

	List<Appointment> findByDoctorId(UUID doctorId);

	List<Appointment> findByDoctorIdAndAppointmentDate(UUID doctorId, LocalDate appointmentDate);

	boolean existsByDoctorIdAndAppointmentDateAndStartTime(UUID doctorId, LocalDate appointmentDate,
			LocalTime startTime);
}