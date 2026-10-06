package com.mediverse.consultationservice.service;

import java.util.UUID;

import com.mediverse.consultationservice.dto.ConsultationRequest;
import com.mediverse.consultationservice.dto.ConsultationResponse;

public interface ConsultationService {

    ConsultationResponse createConsultation(
            UUID userId,
            String token,
            ConsultationRequest request
    );

    ConsultationResponse getConsultation(
            UUID userId,
            UUID consultationId
    );

    ConsultationResponse startConsultation(
            UUID userId,
            UUID consultationId
    );

    ConsultationResponse endConsultation(
            UUID userId,
            UUID consultationId
    );
}