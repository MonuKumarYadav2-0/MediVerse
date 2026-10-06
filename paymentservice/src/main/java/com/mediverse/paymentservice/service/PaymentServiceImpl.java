package com.mediverse.paymentservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mediverse.paymentservice.dto.PaymentRequest;
import com.mediverse.paymentservice.dto.PaymentResponse;
import com.mediverse.paymentservice.entity.Payment;
import com.mediverse.paymentservice.entity.PaymentStatus;
import com.mediverse.paymentservice.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepository;

	// ==========================================
	// CREATE PAYMENT
	// ==========================================

	@Override
	public PaymentResponse createPayment(UUID patientId, PaymentRequest request) {

		boolean alreadyExists = paymentRepository.existsByReferenceIdAndReferenceType(request.getReferenceId(),
				request.getReferenceType());

		if (alreadyExists) {
			throw new RuntimeException("Payment already exists for this transaction");
		}

		Payment payment = Payment.builder().patientId(patientId).referenceId(request.getReferenceId())
				.referenceType(request.getReferenceType()).amount(request.getAmount())
				.paymentMethod(request.getPaymentMethod()).status(PaymentStatus.PENDING).createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now()).build();

		return mapToResponse(paymentRepository.save(payment));
	}

	// ==========================================
	// GET PARTICULAR PAYMENT
	// ==========================================

	@Override
	@Transactional(readOnly = true)
	public PaymentResponse getPayment(UUID patientId, UUID paymentId) {

		Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow(() -> new RuntimeException("Payment not found"));

		if (!payment.getPatientId().equals(patientId)) {

			throw new RuntimeException("You cannot access this payment");
		}

		return mapToResponse(payment);
	}

	// ==========================================
	// MY PAYMENTS
	// ==========================================

	@Override
	@Transactional(readOnly = true)
	public List<PaymentResponse> getMyPayments(UUID patientId) {

		return paymentRepository.findByPatientId(patientId).stream().map(this::mapToResponse).toList();
	}

	// ==========================================
	// UPDATE PAYMENT STATUS
	// ==========================================

	@Override
	public PaymentResponse updatePaymentStatus(UUID paymentId, PaymentStatus status) {

		Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow(() -> new RuntimeException("Payment not found"));

		validateStatusTransition(payment.getStatus(), status);

		payment.setStatus(status);
		payment.setUpdatedAt(LocalDateTime.now());

		return mapToResponse(payment);
	}

	// ==========================================
	// STATUS TRANSITION
	// ==========================================

	private void validateStatusTransition(PaymentStatus current, PaymentStatus next) {

		boolean valid = switch (current) {

		case PENDING -> next == PaymentStatus.SUCCESS || next == PaymentStatus.FAILED;

		case SUCCESS -> next == PaymentStatus.REFUNDED;

		case FAILED, REFUNDED -> false;
		};

		if (!valid) {
			throw new RuntimeException("Invalid payment status transition: " + current + " -> " + next);
		}
	}

	// ==========================================
	// MAPPING
	// ==========================================

	private PaymentResponse mapToResponse(Payment payment) {

		return PaymentResponse.builder().id(payment.getId()).patientId(payment.getPatientId())
				.referenceId(payment.getReferenceId()).referenceType(payment.getReferenceType().name())
				.amount(payment.getAmount()).paymentMethod(payment.getPaymentMethod().name())
				.status(payment.getStatus().name()).createdAt(payment.getCreatedAt()).updatedAt(payment.getUpdatedAt())
				.build();
	}
}