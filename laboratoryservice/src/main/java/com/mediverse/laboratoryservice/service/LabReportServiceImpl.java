package com.mediverse.laboratoryservice.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mediverse.laboratoryservice.dto.LabReportRequest;
import com.mediverse.laboratoryservice.dto.LabReportResponse;
import com.mediverse.laboratoryservice.entity.BookingStatus;
import com.mediverse.laboratoryservice.entity.LabBooking;
import com.mediverse.laboratoryservice.entity.LabReport;
import com.mediverse.laboratoryservice.entity.Laboratory;
import com.mediverse.laboratoryservice.repository.LabBookingRepository;
import com.mediverse.laboratoryservice.repository.LabReportRepository;
import com.mediverse.laboratoryservice.repository.LaboratoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LabReportServiceImpl implements LabReportService {

    private final LabReportRepository reportRepository;
    private final LabBookingRepository bookingRepository;
    private final LaboratoryRepository laboratoryRepository;

    @Override
    public LabReportResponse uploadReport(
            UUID userId,
            UUID bookingId,
            LabReportRequest request) {

        Laboratory laboratory =
                laboratoryRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Laboratory profile not found"
                                )
                        );

        LabBooking booking =
                bookingRepository.findById(bookingId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Booking not found"
                                )
                        );

        if (!booking.getLaboratoryId()
                .equals(laboratory.getId())) {

            throw new RuntimeException(
                    "You cannot upload report for this booking"
            );
        }

        if (booking.getStatus()
                != BookingStatus.COMPLETED) {

            throw new RuntimeException(
                    "Report can be uploaded only after test completion"
            );
        }

        if (reportRepository.findByBookingId(bookingId)
                .isPresent()) {

            throw new RuntimeException(
                    "Report already exists for this booking"
            );
        }

        LabReport report = LabReport.builder()
                .bookingId(bookingId)
                .reportUrl(request.getReportUrl())
                .remarks(request.getRemarks())
                .generatedAt(LocalDateTime.now())
                .build();

        return mapToResponse(
                reportRepository.save(report)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public LabReportResponse getReport(
            UUID patientId,
            UUID bookingId) {

        LabBooking booking =
                bookingRepository.findById(bookingId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Booking not found"
                                )
                        );

        if (!booking.getPatientId()
                .equals(patientId)) {

            throw new RuntimeException(
                    "You cannot access this report"
            );
        }

        LabReport report =
                reportRepository.findByBookingId(bookingId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Report not found"
                                )
                        );

        return mapToResponse(report);
    }

    private LabReportResponse mapToResponse(
            LabReport report) {

        return LabReportResponse.builder()
                .id(report.getId())
                .bookingId(report.getBookingId())
                .reportUrl(report.getReportUrl())
                .remarks(report.getRemarks())
                .generatedAt(report.getGeneratedAt())
                .build();
    }
}