package com.mediverse.consultationservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mediverse.consultationservice.entity.Consultation;

public interface ConsultationRepository
        extends JpaRepository<Consultation, UUID> {

    Optional<Consultation> findByAppointmentId(UUID appointmentId);

    boolean existsByAppointmentId(UUID appointmentId);

    Optional<Consultation> findByMeetingRoomId(String meetingRoomId);
}