package com.mediverse.laboratoryservice.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mediverse.laboratoryservice.dto.LaboratoryRequest;
import com.mediverse.laboratoryservice.dto.LaboratoryResponse;
import com.mediverse.laboratoryservice.entity.Laboratory;
import com.mediverse.laboratoryservice.repository.LaboratoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LaboratoryServiceImpl implements LaboratoryService {

	private final LaboratoryRepository laboratoryRepository;

	@Override
	public LaboratoryResponse registerLab(UUID userId, LaboratoryRequest request) {

		if (laboratoryRepository.existsByUserId(userId)) {
			throw new RuntimeException("Laboratory profile already exists");
		}

		Laboratory laboratory = Laboratory.builder().userId(userId).labName(request.getLabName())
				.address(request.getAddress()).phone(request.getPhone()).active(true).build();

		Laboratory saved = laboratoryRepository.save(laboratory);

		return mapToResponse(saved);
	}

	@Override
	@Transactional(readOnly = true)
	public LaboratoryResponse getMyLab(UUID userId) {

		Laboratory laboratory = laboratoryRepository.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("Laboratory profile not found"));

		return mapToResponse(laboratory);
	}

	@Override
	public LaboratoryResponse updateMyLab(UUID userId, LaboratoryRequest request) {

		Laboratory laboratory = laboratoryRepository.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("Laboratory profile not found"));

		laboratory.setLabName(request.getLabName());
		laboratory.setAddress(request.getAddress());
		laboratory.setPhone(request.getPhone());

		return mapToResponse(laboratory);
	}

	private LaboratoryResponse mapToResponse(Laboratory laboratory) {

		return LaboratoryResponse.builder().id(laboratory.getId()).userId(laboratory.getUserId())
				.labName(laboratory.getLabName()).address(laboratory.getAddress()).phone(laboratory.getPhone())
				.active(laboratory.getActive()).build();
	}
}