package com.mediverse.pharmacyservice.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mediverse.pharmacyservice.dto.ExternalPrescriptionResponse;
import com.mediverse.pharmacyservice.entity.ExternalPrescription;
import com.mediverse.pharmacyservice.entity.PrescriptionStatus;
import com.mediverse.pharmacyservice.repository.ExternalPrescriptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExternalPrescriptionServiceImpl
        implements ExternalPrescriptionService {

    private final ExternalPrescriptionRepository repository;

    @Value("${file.upload-dir:uploads/prescriptions}")
    private String uploadDir;

    @Override
    public ExternalPrescriptionResponse uploadPrescription(
            UUID patientId,
            MultipartFile file) {

        validateFile(file);

        try {

            Path directory =
                    Paths.get(uploadDir);

            Files.createDirectories(directory);

            String originalFileName =
                    file.getOriginalFilename();

            String extension = "";

            if (originalFileName != null
                    && originalFileName.contains(".")) {

                extension =
                        originalFileName.substring(
                                originalFileName.lastIndexOf(".")
                        );
            }

            String storedFileName =
                    UUID.randomUUID() + extension;

            Path filePath =
                    directory.resolve(storedFileName);

            Files.copy(
                    file.getInputStream(),
                    filePath
            );

            ExternalPrescription prescription =
                    ExternalPrescription.builder()
                            .patientId(patientId)
                            .fileName(originalFileName)
                            .filePath(
                                    filePath.toString()
                            )
                            .contentType(
                                    file.getContentType()
                            )
                            .status(
                                    PrescriptionStatus.UNDER_REVIEW
                            )
                            .uploadedAt(
                                    LocalDateTime.now()
                            )
                            .build();

            ExternalPrescription saved =
                    repository.save(prescription);

            return mapToResponse(saved);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to upload prescription",
                    e
            );
        }
    }

    @Override
    public List<ExternalPrescriptionResponse>
            getMyPrescriptions(UUID patientId) {

        return repository
                .findByPatientId(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException(
                    "Prescription file is required"
            );
        }

        String contentType =
                file.getContentType();

        if (!"application/pdf".equals(contentType)
                && !"image/jpeg".equals(contentType)
                && !"image/png".equals(contentType)) {

            throw new RuntimeException(
                    "Only PDF, JPG and PNG files are allowed"
            );
        }

        if (file.getSize() > 10 * 1024 * 1024) {

            throw new RuntimeException(
                    "File size cannot exceed 10 MB"
            );
        }
    }

    private ExternalPrescriptionResponse
            mapToResponse(
                    ExternalPrescription prescription) {

        return ExternalPrescriptionResponse.builder()
                .id(prescription.getId())
                .patientId(
                        prescription.getPatientId()
                )
                .fileName(
                        prescription.getFileName()
                )
                .contentType(
                        prescription.getContentType()
                )
                .status(
                        prescription.getStatus()
                )
                .uploadedAt(
                        prescription.getUploadedAt()
                )
                .build();
    }
}