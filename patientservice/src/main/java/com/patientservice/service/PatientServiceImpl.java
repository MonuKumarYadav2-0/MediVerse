package com.patientservice.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.patientservice.dto.PatientRequest;
import com.patientservice.dto.PatientResponse;
import com.patientservice.entity.Patient;
import com.patientservice.exception.PatientAlreadyExistsException;
import com.patientservice.exception.PatientNotFoundException;
import com.patientservice.mapper.PatientMapper;
import com.patientservice.repository.PatientRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

	private final PatientRepository patientRepository;
	private final PatientMapper patientMapper;

	@Override
	@Transactional
	public PatientResponse createPatient(UUID userId, PatientRequest request) {

		if (patientRepository.existsByUserId(userId)) {
			throw new PatientAlreadyExistsException("Patient profile already exists");
		}

		Patient patient = patientMapper.toEntity(request);

		patient.setUserId(userId);
		patient.setActive(true);

		Patient savedPatient = patientRepository.save(patient);

		return patientMapper.toResponse(savedPatient);
	}

	@Override
	@Transactional(readOnly = true)
	public PatientResponse getPatientByUserId(UUID userId) {

		Patient patient = patientRepository.findByUserId(userId)
				.orElseThrow(() -> new PatientNotFoundException("Patient profile not found"));

		return patientMapper.toResponse(patient);
	}

	@Override
	@Transactional(readOnly = true)
	public PatientResponse getPatientById(UUID patientId) {

		Patient patient = patientRepository.findById(patientId)
				.orElseThrow(() -> new PatientNotFoundException("Patient not found"));

		return patientMapper.toResponse(patient);
	}

	@Override
	@Transactional
	public PatientResponse updatePatient(UUID userId, PatientRequest request) {

		Patient patient = patientRepository.findByUserId(userId)
				.orElseThrow(() -> new PatientNotFoundException("Patient profile not found"));

		patient.setDateOfBirth(request.getDateOfBirth());
		patient.setGender(request.getGender());
		patient.setBloodGroup(request.getBloodGroup());
		patient.setAddress(request.getAddress());
		patient.setCity(request.getCity());
		patient.setState(request.getState());
		patient.setPincode(request.getPincode());
		patient.setEmergencyContactName(request.getEmergencyContactName());
		patient.setEmergencyContactPhone(request.getEmergencyContactPhone());

		Patient updatedPatient = patientRepository.save(patient);

		return patientMapper.toResponse(updatedPatient);
	}

	@Override
	@Transactional
	public void deactivatePatient(UUID userId) {

		Patient patient = patientRepository.findByUserId(userId)
				.orElseThrow(() -> new PatientNotFoundException("Patient profile not found"));

		patient.setActive(false);

		patientRepository.save(patient);
	}
}