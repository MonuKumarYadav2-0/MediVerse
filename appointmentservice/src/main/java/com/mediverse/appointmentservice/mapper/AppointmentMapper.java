package com.mediverse.appointmentservice.mapper;

import org.springframework.stereotype.Component;

import com.mediverse.appointmentservice.dto.AppointmentRequest;
import com.mediverse.appointmentservice.dto.AppointmentResponse;
import com.mediverse.appointmentservice.entity.Appointment;

@Component
public class AppointmentMapper {

    public Appointment toEntity(AppointmentRequest request) {

        return Appointment.builder()
                .doctorId(request.getDoctorId())
                .appointmentDate(request.getAppointmentDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .type(request.getType())
                .reason(request.getReason())
                .build();
    }

    public AppointmentResponse toResponse(
            Appointment appointment) {

        return AppointmentResponse.builder()
                .id(appointment.getId())
                .patientId(appointment.getPatientId())
                .doctorId(appointment.getDoctorId())
                .appointmentDate(appointment.getAppointmentDate())
                .startTime(appointment.getStartTime())
                .endTime(appointment.getEndTime())
                .type(appointment.getType())
                .status(appointment.getStatus())
                .reason(appointment.getReason())
                .consultationFee(appointment.getConsultationFee())
                .build();
    }

    public void updateEntity(
            Appointment appointment,
            AppointmentRequest request) {

        appointment.setDoctorId(request.getDoctorId());
        appointment.setAppointmentDate(
                request.getAppointmentDate());
        appointment.setStartTime(request.getStartTime());
        appointment.setEndTime(request.getEndTime());
        appointment.setType(request.getType());
        appointment.setReason(request.getReason());
    }
}