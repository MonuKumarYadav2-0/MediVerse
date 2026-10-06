package com.mediverse.laboratoryservice.service;

import java.util.UUID;

import com.mediverse.laboratoryservice.dto.LabReportRequest;
import com.mediverse.laboratoryservice.dto.LabReportResponse;

public interface LabReportService {

    LabReportResponse uploadReport(
            UUID userId,
            UUID bookingId,
            LabReportRequest request
    );

    LabReportResponse getReport(
            UUID patientId,
            UUID bookingId
    );
}