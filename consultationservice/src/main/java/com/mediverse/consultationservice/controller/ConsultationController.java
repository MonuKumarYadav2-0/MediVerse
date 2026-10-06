package com.mediverse.consultationservice.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mediverse.consultationservice.dto.ConsultationRequest;
import com.mediverse.consultationservice.dto.ConsultationResponse;
import com.mediverse.consultationservice.security.UserPrincipal;
import com.mediverse.consultationservice.service.ConsultationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/consultations")
@RequiredArgsConstructor
public class ConsultationController {

    private final ConsultationService consultationService;

    // Create consultation
    @PostMapping
    public ResponseEntity<ConsultationResponse> createConsultation(
            @Valid @RequestBody ConsultationRequest request,
            Authentication authentication) {

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        UUID userId = principal.getUserId();

        // Get JWT token
        String token =
                authentication.getCredentials().toString();

        ConsultationResponse response =
                consultationService.createConsultation(
                        userId,
                        token,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Get consultation
    @GetMapping("/{consultationId}")
    public ResponseEntity<ConsultationResponse> getConsultation(
            @PathVariable UUID consultationId,
            Authentication authentication) {

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        UUID userId = principal.getUserId();

        ConsultationResponse response =
                consultationService.getConsultation(
                        userId,
                        consultationId
                );

        return ResponseEntity.ok(response);
    }

    // Start consultation
    @PostMapping("/{consultationId}/start")
    public ResponseEntity<ConsultationResponse> startConsultation(
            @PathVariable UUID consultationId,
            Authentication authentication) {

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        UUID userId = principal.getUserId();

        ConsultationResponse response =
                consultationService.startConsultation(
                        userId,
                        consultationId
                );

        return ResponseEntity.ok(response);
    }

    // End consultation
    @PostMapping("/{consultationId}/end")
    public ResponseEntity<ConsultationResponse> endConsultation(
            @PathVariable UUID consultationId,
            Authentication authentication) {

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        UUID userId = principal.getUserId();

        ConsultationResponse response =
                consultationService.endConsultation(
                        userId,
                        consultationId
                );

        return ResponseEntity.ok(response);
    }
}