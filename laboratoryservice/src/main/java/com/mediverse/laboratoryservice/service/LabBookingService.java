package com.mediverse.laboratoryservice.service;

import java.util.List;
import java.util.UUID;

import com.mediverse.laboratoryservice.dto.LabBookingRequest;
import com.mediverse.laboratoryservice.dto.LabBookingResponse;
import com.mediverse.laboratoryservice.entity.BookingStatus;

public interface LabBookingService {

    LabBookingResponse createBooking(
            UUID patientId,
            LabBookingRequest request
    );

    List<LabBookingResponse> getMyBookings(
            UUID patientId
    );

    LabBookingResponse getBooking(
            UUID patientId,
            UUID bookingId
    );

    List<LabBookingResponse> getLaboratoryBookings(
            UUID userId
    );

    LabBookingResponse updateStatus(
            UUID userId,
            UUID bookingId,
            BookingStatus status
    );
}