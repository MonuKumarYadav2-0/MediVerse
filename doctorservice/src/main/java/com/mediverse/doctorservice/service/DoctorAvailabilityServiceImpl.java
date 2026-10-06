package com.mediverse.doctorservice.service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mediverse.doctorservice.dto.DoctorAvailabilityRequest;
import com.mediverse.doctorservice.dto.DoctorAvailabilityResponse;
import com.mediverse.doctorservice.entity.Doctor;
import com.mediverse.doctorservice.entity.DoctorAvailability;
import com.mediverse.doctorservice.mapper.DoctorAvailabilityMapper;
import com.mediverse.doctorservice.repository.DoctorAvailabilityRepository;
import com.mediverse.doctorservice.repository.DoctorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorAvailabilityServiceImpl implements DoctorAvailabilityService {

	private final DoctorAvailabilityRepository availabilityRepository;
	private final DoctorRepository doctorRepository;
	private final DoctorAvailabilityMapper availabilityMapper;

	@Override
	@Transactional
	public DoctorAvailabilityResponse createAvailability(UUID userId, DoctorAvailabilityRequest request) {

		// 1. Find doctor using authenticated user's userId
		Doctor doctor = doctorRepository.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("Doctor profile not found"));

		// 2. Validate time
		if (!request.getStartTime().isBefore(request.getEndTime())) {

			throw new RuntimeException("Start time must be before end time");
		}

		// 3. Check duplicate day
		if (availabilityRepository.existsByDoctorIdAndDayOfWeek(doctor.getId(), request.getDayOfWeek())) {

			throw new RuntimeException("Availability already exists for this day");
		}

		// 4. DTO → Entity
		DoctorAvailability availability = availabilityMapper.toEntity(request);

		// 5. Set doctorId from authenticated doctor
		availability.setDoctorId(userId);

		// 6. Save
		DoctorAvailability saved = availabilityRepository.save(availability);

		// 7. Entity → Response
		return availabilityMapper.toResponse(saved);
	}

	@Override
	@Transactional(readOnly = true)
	public List<DoctorAvailabilityResponse> getMyAvailability(UUID userId) {

		Doctor doctor = doctorRepository.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("Doctor profile not found"));

		return availabilityRepository.findByDoctorId(doctor.getId()).stream().map(availabilityMapper::toResponse)
				.toList();
	}

	@Override
	@Transactional
	public DoctorAvailabilityResponse updateAvailability(UUID userId, UUID availabilityId,
			DoctorAvailabilityRequest request) {

		Doctor doctor = doctorRepository.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("Doctor profile not found"));

		DoctorAvailability availability = availabilityRepository.findById(availabilityId)
				.orElseThrow(() -> new RuntimeException("Availability not found"));

		// Make sure this availability belongs to
		// the currently logged-in doctor
		if (!availability.getDoctorId().equals(doctor.getId())) {

			throw new RuntimeException("You cannot modify this availability");
		}

		// Validate time
		if (!request.getStartTime().isBefore(request.getEndTime())) {

			throw new RuntimeException("Start time must be before end time");
		}

		// If changing the day, check duplicate
		if (!availability.getDayOfWeek().equals(request.getDayOfWeek())
				&& availabilityRepository.existsByDoctorIdAndDayOfWeek(doctor.getId(), request.getDayOfWeek())) {

			throw new RuntimeException("Availability already exists for this day");
		}

		availabilityMapper.updateEntity(availability, request);

		DoctorAvailability updated = availabilityRepository.save(availability);

		return availabilityMapper.toResponse(updated);
	}

	@Override
	@Transactional
	public void deleteAvailability(UUID userId, UUID availabilityId) {

		Doctor doctor = doctorRepository.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("Doctor profile not found"));

		DoctorAvailability availability = availabilityRepository.findById(availabilityId)
				.orElseThrow(() -> new RuntimeException("Availability not found"));

		// Ownership check
		if (!availability.getDoctorId().equals(doctor.getId())) {

			throw new RuntimeException("You cannot delete this availability");
		}

		availabilityRepository.delete(availability);
	}

	@Override
	@Transactional(readOnly = true)
	public List<DoctorAvailabilityResponse> getDoctorAvailability(UUID doctorId) {

		return availabilityRepository.findByDoctorId(doctorId).stream().map(availabilityMapper::toResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public DoctorAvailabilityResponse getDoctorAvailability(UUID doctorId, DayOfWeek dayOfWeek) {

		DoctorAvailability availability = availabilityRepository.findByDoctorIdAndDayOfWeek(doctorId, dayOfWeek)
				.orElseThrow(() -> new RuntimeException("Doctor is not available on this day"));

		return availabilityMapper.toResponse(availability);
	}
}