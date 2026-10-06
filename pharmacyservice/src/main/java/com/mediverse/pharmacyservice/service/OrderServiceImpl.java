package com.mediverse.pharmacyservice.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mediverse.pharmacyservice.dto.CreateOrderRequest;
import com.mediverse.pharmacyservice.dto.OrderItemRequest;
import com.mediverse.pharmacyservice.dto.OrderItemResponse;
import com.mediverse.pharmacyservice.dto.OrderResponse;
import com.mediverse.pharmacyservice.dto.OrderSourceType;
import com.mediverse.pharmacyservice.entity.Medicine;
import com.mediverse.pharmacyservice.entity.OrderItem;
import com.mediverse.pharmacyservice.entity.OrderStatus;
import com.mediverse.pharmacyservice.entity.Pharmacy;
import com.mediverse.pharmacyservice.entity.PharmacyOrder;
import com.mediverse.pharmacyservice.repository.MedicineRepository;
import com.mediverse.pharmacyservice.repository.OrderItemRepository;
import com.mediverse.pharmacyservice.repository.PharmacyOrderRepository;
import com.mediverse.pharmacyservice.repository.PharmacyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

	private final PharmacyOrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final MedicineRepository medicineRepository;
	private final PharmacyRepository pharmacyRepository;

	// =========================================================
	// PATIENT SIDE - CREATE ORDER
	// =========================================================

	@Override
	public OrderResponse createOrder(UUID patientId, CreateOrderRequest request) {

		// Check pharmacy
		pharmacyRepository.findById(request.getPharmacyId())
				.orElseThrow(() -> new RuntimeException("Pharmacy not found"));

		if (request.getItems() == null || request.getItems().isEmpty()) {

			throw new RuntimeException("Order must contain at least one medicine");
		}

		PharmacyOrder order = PharmacyOrder.builder().patientId(patientId).pharmacyId(request.getPharmacyId())
				.prescriptionId(request.getPrescriptionId()).sourceType(mapSourceType(request.getSourceType()))
				.status(OrderStatus.PLACED).totalAmount(BigDecimal.ZERO).createdAt(LocalDateTime.now()).build();

		PharmacyOrder savedOrder = orderRepository.save(order);

		BigDecimal totalAmount = BigDecimal.ZERO;

		List<OrderItem> orderItems = new ArrayList<>();

		for (OrderItemRequest itemRequest : request.getItems()) {

			Medicine medicine = medicineRepository.findById(itemRequest.getMedicineId())
					.orElseThrow(() -> new RuntimeException("Medicine not found"));

			// Medicine belongs to selected pharmacy
			if (!medicine.getPharmacyId().equals(request.getPharmacyId())) {

				throw new RuntimeException("Medicine does not belong to this pharmacy");
			}

			// Medicine active?
			if (!Boolean.TRUE.equals(medicine.getActive())) {

				throw new RuntimeException(medicine.getMedicineName() + " is not available");
			}

			// Stock check
			if (medicine.getAvailableQuantity() < itemRequest.getQuantity()) {

				throw new RuntimeException("Insufficient stock for " + medicine.getMedicineName());
			}

			BigDecimal subtotal = medicine.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

			OrderItem orderItem = OrderItem.builder().orderId(savedOrder.getId()).medicineId(medicine.getId())
					.medicineName(medicine.getMedicineName()).quantity(itemRequest.getQuantity())
					.price(medicine.getPrice()).subtotal(subtotal).build();

			orderItems.add(orderItem);

			totalAmount = totalAmount.add(subtotal);

			// Reduce stock
			medicine.setAvailableQuantity(medicine.getAvailableQuantity() - itemRequest.getQuantity());
		}

		orderItemRepository.saveAll(orderItems);

		savedOrder.setTotalAmount(totalAmount);

		return mapToResponse(savedOrder, orderItems);
	}

	// =========================================================
	// PATIENT SIDE - GET PARTICULAR ORDER
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public OrderResponse getOrder(UUID patientId, UUID orderId) {

		PharmacyOrder order = orderRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Order not found"));

		// Patient can access only own order
		if (!order.getPatientId().equals(patientId)) {

			throw new RuntimeException("You cannot access this order");
		}

		List<OrderItem> items = orderItemRepository.findByOrderId(orderId);

		return mapToResponse(order, items);
	}

	// =========================================================
	// PATIENT SIDE - MY ORDERS
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public List<OrderResponse> getMyOrders(UUID patientId) {

		return orderRepository.findByPatientId(patientId).stream().map(order -> {

			List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());

			return mapToResponse(order, items);
		}).toList();
	}

	// =========================================================
	// PHARMACY SIDE - ALL MY ORDERS
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public List<OrderResponse> getPharmacyOrders(UUID userId) {

		Pharmacy pharmacy = pharmacyRepository.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("Pharmacy profile not found"));

		return orderRepository.findByPharmacyId(pharmacy.getId()).stream().map(order -> {

			List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());

			return mapToResponse(order, items);
		}).toList();
	}

	// =========================================================
	// PHARMACY SIDE - PARTICULAR ORDER
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public OrderResponse getPharmacyOrder(UUID userId, UUID orderId) {

		Pharmacy pharmacy = pharmacyRepository.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("Pharmacy profile not found"));

		PharmacyOrder order = orderRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Order not found"));

		// Security check
		if (!order.getPharmacyId().equals(pharmacy.getId())) {

			throw new RuntimeException("You cannot access this order");
		}

		List<OrderItem> items = orderItemRepository.findByOrderId(orderId);

		return mapToResponse(order, items);
	}

	// =========================================================
	// PHARMACY SIDE - UPDATE STATUS
	// =========================================================

	@Override
	public OrderResponse updateOrderStatus(UUID userId, UUID orderId, OrderStatus newStatus) {

		Pharmacy pharmacy = pharmacyRepository.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("Pharmacy profile not found"));

		PharmacyOrder order = orderRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Order not found"));

		// Pharmacy ownership check
		if (!order.getPharmacyId().equals(pharmacy.getId())) {

			throw new RuntimeException("You cannot update this order");
		}

		// Validate transition
		validateStatusTransition(order.getStatus(), newStatus);

		order.setStatus(newStatus);

		List<OrderItem> items = orderItemRepository.findByOrderId(orderId);

		return mapToResponse(order, items);
	}

	// =========================================================
	// STATUS TRANSITION
	// =========================================================

	private void validateStatusTransition(OrderStatus current, OrderStatus next) {

		boolean valid = switch (current) {

		case PLACED -> next == OrderStatus.CONFIRMED || next == OrderStatus.CANCELLED;

		case CONFIRMED -> next == OrderStatus.PREPARING || next == OrderStatus.CANCELLED;

		case PREPARING -> next == OrderStatus.READY;

		case READY -> next == OrderStatus.DELIVERED;

		case DELIVERED, CANCELLED -> false;
		};

		if (!valid) {

			throw new RuntimeException("Invalid order status transition: " + current + " -> " + next);
		}
	}

	// =========================================================
	// MAPPING
	// =========================================================

	private OrderResponse mapToResponse(PharmacyOrder order, List<OrderItem> items) {

		List<OrderItemResponse> itemResponses = items.stream()
				.map(item -> OrderItemResponse.builder().id(item.getId()).medicineId(item.getMedicineId())
						.medicineName(item.getMedicineName()).quantity(item.getQuantity()).price(item.getPrice())
						.subtotal(item.getSubtotal()).build())
				.toList();

		return OrderResponse.builder().id(order.getId()).patientId(order.getPatientId())
				.pharmacyId(order.getPharmacyId()).prescriptionId(order.getPrescriptionId())
				.sourceType(mapSourceTypeForResponse(order.getSourceType())).status(order.getStatus().name())
				.totalAmount(order.getTotalAmount()).createdAt(order.getCreatedAt()).items(itemResponses).build();
	}

	// =========================================================
	// SOURCE TYPE MAPPING
	// =========================================================

	private com.mediverse.pharmacyservice.entity.OrderSourceType mapSourceType(OrderSourceType sourceType) {

		return com.mediverse.pharmacyservice.entity.OrderSourceType.valueOf(sourceType.name());
	}

	private OrderSourceType mapSourceTypeForResponse(com.mediverse.pharmacyservice.entity.OrderSourceType sourceType) {

		return OrderSourceType.valueOf(sourceType.name());
	}
}