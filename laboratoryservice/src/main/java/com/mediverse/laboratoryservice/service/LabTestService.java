package com.mediverse.laboratoryservice.service;

import java.util.List;
import java.util.UUID;

import com.mediverse.laboratoryservice.dto.LabTestRequest;
import com.mediverse.laboratoryservice.dto.LabTestResponse;

public interface LabTestService {

    LabTestResponse addTest(
            UUID userId,
            LabTestRequest request
    );

    List<LabTestResponse> getMyTests(
            UUID userId
    );

    LabTestResponse updateTest(
            UUID userId,
            UUID testId,
            LabTestRequest request
    );

    void deactivateTest(
            UUID userId,
            UUID testId
    );

    List<LabTestResponse> searchTests(
            String name
    );
}