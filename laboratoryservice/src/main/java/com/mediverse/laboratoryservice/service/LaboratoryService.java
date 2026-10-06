package com.mediverse.laboratoryservice.service;

import java.util.UUID;

import com.mediverse.laboratoryservice.dto.LaboratoryRequest;
import com.mediverse.laboratoryservice.dto.LaboratoryResponse;

public interface LaboratoryService {

	LaboratoryResponse registerLab(UUID userId, LaboratoryRequest request);

	LaboratoryResponse getMyLab(UUID userId);

	LaboratoryResponse updateMyLab(UUID userId, LaboratoryRequest request);
}