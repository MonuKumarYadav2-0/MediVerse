package com.mediverse.laboratoryservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mediverse.laboratoryservice.dto.LabBookingRequest;
import com.mediverse.laboratoryservice.dto.LabBookingResponse;
import com.mediverse.laboratoryservice.entity.BookingStatus;
import com.mediverse.laboratoryservice.entity.LabBooking;
import com.mediverse.laboratoryservice.entity.LabTest;
import com.mediverse.laboratoryservice.entity.Laboratory;
import com.mediverse.laboratoryservice.repository.LabBookingRepository;
import com.mediverse.laboratoryservice.repository.LabTestRepository;
import com.mediverse.laboratoryservice.repository.LaboratoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LabBookingServiceImpl implements LabBookingService {

	private final LabBookingRepository bookingRepository;
	private final LaboratoryRepository laboratoryRepository;
	private final LabTestRepository labTestRepository;

	@Override
	public LabBookingResponse createBooking(UUID patientId, LabBookingRequest request) {

		Laboratory laboratory = laboratoryRepository.findById(request.getLaboratoryId())
				.orElseThrow(() -> new RuntimeException("Laboratory not found"));

		if (!Boolean.TRUE.equals(laboratory.getActive())) {
			throw new RuntimeException("Laboratory is not active");
		}

		LabTest test = labTestRepository.findById(request.getTestId())
				.orElseThrow(() -> new RuntimeException("Lab test not found"));

		if (!test.getLaboratoryId().equals(laboratory.getId())) {

			throw new RuntimeException("Test does not belong to this laboratory");
		}

		if (!Boolean.TRUE.equals(test.getActive())) {
			throw new RuntimeException("Lab test is not available");
		}

		LabBooking booking = LabBooking.builder().patientId(patientId).laboratoryId(laboratory.getId())
				.testId(test.getId()).bookingDate(request.getBookingDate()).status(BookingStatus.BOOKED)
				.amount(test.getPrice()).createdAt(LocalDateTime.now()).build();

		return mapToResponse(bookingRepository.save(booking));
	}

	@Override
	@Transactional(readOnly = true)
	public List<LabBookingResponse> getMyBookings(UUID patientId) {

		return bookingRepository.findByPatientId(patientId).stream().map(this::mapToResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public LabBookingResponse getBooking(UUID patientId, UUID bookingId) {

		LabBooking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking not found"));

		if (!booking.getPatientId().equals(patientId)) {

			throw new RuntimeException("You cannot access this booking");
		}

		return mapToResponse(booking);
	}

	@Override
	@Transactional(readOnly = true)
	public List<LabBookingResponse> getLaboratoryBookings(UUID userId) {

		Laboratory laboratory = laboratoryRepository.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("Laboratory profile not found"));

		return bookingRepository.findByLaboratoryId(laboratory.getId()).stream().map(this::mapToResponse).toList();
	}

	@Override
	public LabBookingResponse updateStatus(UUID userId, UUID bookingId, BookingStatus status) {

		Laboratory laboratory = laboratoryRepository.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("Laboratory profile not found"));

		LabBooking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking not found"));

		if (!booking.getLaboratoryId().equals(laboratory.getId())) {

			throw new RuntimeException("You cannot update this booking");
		}

		validateStatusTransition(booking.getStatus(), status);

		booking.setStatus(status);

		return mapToResponse(booking);
	}

	private void validateStatusTransition(BookingStatus current, BookingStatus next) {

		boolean valid = switch (current) {

		case BOOKED -> next == BookingStatus.CONFIRMED || next == BookingStatus.CANCELLED;

		case CONFIRMED -> next == BookingStatus.SAMPLE_COLLECTED || next == BookingStatus.CANCELLED;

		case SAMPLE_COLLECTED -> next == BookingStatus.PROCESSING;

		case PROCESSING -> next == BookingStatus.COMPLETED;

		case COMPLETED, CANCELLED -> false;
		};

		if (!valid) {
			throw new RuntimeException("Invalid booking status transition: " + current + " -> " + next);
		}
	}

	private LabBookingResponse mapToResponse(LabBooking booking) {

		return LabBookingResponse.builder().id(booking.getId()).patientId(booking.getPatientId())
				.laboratoryId(booking.getLaboratoryId()).testId(booking.getTestId())
				.bookingDate(booking.getBookingDate()).status(booking.getStatus().name()).amount(booking.getAmount())
				.createdAt(booking.getCreatedAt()).build();
	}
}