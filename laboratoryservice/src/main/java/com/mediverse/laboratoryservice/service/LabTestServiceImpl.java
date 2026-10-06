package com.mediverse.laboratoryservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mediverse.laboratoryservice.dto.LabTestRequest;
import com.mediverse.laboratoryservice.dto.LabTestResponse;
import com.mediverse.laboratoryservice.entity.LabTest;
import com.mediverse.laboratoryservice.entity.Laboratory;
import com.mediverse.laboratoryservice.repository.LabTestRepository;
import com.mediverse.laboratoryservice.repository.LaboratoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LabTestServiceImpl implements LabTestService {

    private final LabTestRepository labTestRepository;
    private final LaboratoryRepository laboratoryRepository;

    @Override
    public LabTestResponse addTest(
            UUID userId,
            LabTestRequest request) {

        Laboratory laboratory =
                laboratoryRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Laboratory profile not found"
                                )
                        );

        LabTest test = LabTest.builder()
                .laboratoryId(laboratory.getId())
                .testName(request.getTestName())
                .description(request.getDescription())
                .price(request.getPrice())
                .active(true)
                .build();

        return mapToResponse(
                labTestRepository.save(test)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabTestResponse> getMyTests(
            UUID userId) {

        Laboratory laboratory =
                laboratoryRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Laboratory profile not found"
                                )
                        );

        return labTestRepository
                .findByLaboratoryIdAndActiveTrue(
                        laboratory.getId()
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public LabTestResponse updateTest(
            UUID userId,
            UUID testId,
            LabTestRequest request) {

        Laboratory laboratory =
                laboratoryRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Laboratory profile not found"
                                )
                        );

        LabTest test =
                labTestRepository.findById(testId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Lab test not found"
                                )
                        );

        if (!test.getLaboratoryId()
                .equals(laboratory.getId())) {

            throw new RuntimeException(
                    "You can update only your own test"
            );
        }

        test.setTestName(request.getTestName());
        test.setDescription(request.getDescription());
        test.setPrice(request.getPrice());

        return mapToResponse(test);
    }

    @Override
    public void deactivateTest(
            UUID userId,
            UUID testId) {

        Laboratory laboratory =
                laboratoryRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Laboratory profile not found"
                                )
                        );

        LabTest test =
                labTestRepository.findById(testId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Lab test not found"
                                )
                        );

        if (!test.getLaboratoryId()
                .equals(laboratory.getId())) {

            throw new RuntimeException(
                    "You can deactivate only your own test"
            );
        }

        test.setActive(false);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabTestResponse> searchTests(
            String name) {

        return labTestRepository
                .findByTestNameContainingIgnoreCaseAndActiveTrue(name)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private LabTestResponse mapToResponse(
            LabTest test) {

        return LabTestResponse.builder()
                .id(test.getId())
                .laboratoryId(test.getLaboratoryId())
                .testName(test.getTestName())
                .description(test.getDescription())
                .price(test.getPrice())
                .active(test.getActive())
                .build();
    }
}