package com.mediverse.appointmentservice.service;

import java.util.List;
import java.util.UUID;

import com.mediverse.appointmentservice.dto.AppointmentRequest;
import com.mediverse.appointmentservice.dto.AppointmentResponse;

public interface AppointmentService {

    // Patient books an appointment
    AppointmentResponse createAppointment(
            UUID patientId,
            AppointmentRequest request
    );

    // Patient gets his/her appointments
    List<AppointmentResponse> getMyAppointments(
            UUID patientId
    );

    // Get a particular appointment
    AppointmentResponse getAppointmentById(
            UUID appointmentId
    );

    // Cancel patient's appointment
    void cancelAppointment(
            UUID patientId,
            UUID appointmentId
    );

    // Doctor gets his/her appointments
    List<AppointmentResponse> getDoctorAppointments(
            UUID doctorId
    );

    // Doctor confirms appointment
    AppointmentResponse confirmAppointment(
            UUID doctorId,
            UUID appointmentId
    );

    // Doctor marks appointment completed
    AppointmentResponse completeAppointment(
            UUID doctorId,
            UUID appointmentId
    );
}