package com.mediverse.pharmacyservice.service;

import java.util.List;
import java.util.UUID;

import com.mediverse.pharmacyservice.dto.CreateOrderRequest;
import com.mediverse.pharmacyservice.dto.OrderResponse;
import com.mediverse.pharmacyservice.entity.OrderStatus;

public interface OrderService {

	OrderResponse createOrder(UUID patientId, CreateOrderRequest request);

	OrderResponse getOrder(UUID patientId, UUID orderId);

	List<OrderResponse> getMyOrders(UUID patientId);

	List<OrderResponse> getPharmacyOrders(UUID userId);

	OrderResponse updateOrderStatus(UUID userId, UUID orderId, OrderStatus newStatus);

	OrderResponse getPharmacyOrder(UUID userId, UUID orderId);
	
}