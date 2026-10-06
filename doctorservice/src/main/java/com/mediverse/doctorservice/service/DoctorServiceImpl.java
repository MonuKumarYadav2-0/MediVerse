package com.mediverse.doctorservice.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mediverse.doctorservice.dto.DoctorRequest;
import com.mediverse.doctorservice.dto.DoctorResponse;
import com.mediverse.doctorservice.entity.Doctor;
import com.mediverse.doctorservice.mapper.DoctorMapper;
import com.mediverse.doctorservice.repository.DoctorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

	private final DoctorRepository doctorRepository;
	private final DoctorMapper doctorMapper;

	@Override
	@Transactional
	public DoctorResponse createDoctor(UUID userId, DoctorRequest request) {

		// One Auth user can have only one doctor profile
		if (doctorRepository.existsByUserId(userId)) {
			throw new RuntimeException("Doctor profile already exists");
		}

		// Registration number must be unique
		if (doctorRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {

			throw new RuntimeException("Doctor registration number already exists");
		}

		// DTO → Entity
		Doctor doctor = doctorMapper.toEntity(request);

		// IMPORTANT:
		// userId comes from validated JWT,
		// NOT from the request body.
		doctor.setUserId(userId);

		// Save
		Doctor savedDoctor = doctorRepository.save(doctor);

		// Entity → Response DTO
		return doctorMapper.toResponse(savedDoctor);
	}

	@Override
	@Transactional(readOnly = true)
	public DoctorResponse getCurrentDoctor(UUID userId) {

		Doctor doctor = doctorRepository.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("Doctor profile not found"));

		return doctorMapper.toResponse(doctor);
	}

	@Override
	@Transactional
	public DoctorResponse updateDoctor(UUID userId, DoctorRequest request) {

		Doctor doctor = doctorRepository.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("Doctor profile not found"));

		// If registration number is being changed,
		// make sure another doctor isn't already using it.
		if (!doctor.getRegistrationNumber().equals(request.getRegistrationNumber())
				&& doctorRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {

			throw new RuntimeException("Doctor registration number already exists");
		}

		doctorMapper.updateEntity(doctor, request);

		Doctor updatedDoctor = doctorRepository.save(doctor);

		return doctorMapper.toResponse(updatedDoctor);
	}

	@Override
	@Transactional(readOnly = true)
	public DoctorResponse getDoctorById(UUID doctorId) {

		Doctor doctor = doctorRepository.findByUserId(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found"));

		return doctorMapper.toResponse(doctor);
	}

	@Override
	@Transactional
	public void deleteCurrentDoctor(UUID userId) {

		Doctor doctor = doctorRepository.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("Doctor profile not found"));

		// For now, deactivate instead of physically deleting.
		doctor.setStatus("INACTIVE");

		doctorRepository.save(doctor);
	}

	
}