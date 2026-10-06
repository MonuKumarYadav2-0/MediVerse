package com.mediverse.consultationservice.mapper;

import org.springframework.stereotype.Component;

import com.mediverse.consultationservice.dto.ConsultationResponse;
import com.mediverse.consultationservice.entity.Consultation;

@Component
public class ConsultationMapper {

    public ConsultationResponse toResponse(Consultation consultation) {

        return ConsultationResponse.builder()
                .id(consultation.getId())
                .appointmentId(consultation.getAppointmentId())
                .patientId(consultation.getPatientId())
                .doctorId(consultation.getDoctorId())
                .scheduledAt(consultation.getScheduledAt())
                .startedAt(consultation.getStartedAt())
                .endedAt(consultation.getEndedAt())
                .status(consultation.getStatus())
                .meetingRoomId(consultation.getMeetingRoomId())
                .createdAt(consultation.getCreatedAt())
                .updatedAt(consultation.getUpdatedAt())
                .build();
    }
}