package com.mediverse.pharmacyservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mediverse.pharmacyservice.dto.MedicineRequest;
import com.mediverse.pharmacyservice.dto.MedicineResponse;
import com.mediverse.pharmacyservice.entity.Medicine;
import com.mediverse.pharmacyservice.entity.Pharmacy;
import com.mediverse.pharmacyservice.repository.MedicineRepository;
import com.mediverse.pharmacyservice.repository.PharmacyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicineServiceImpl implements MedicineService {

	private final MedicineRepository medicineRepository;
	private final PharmacyRepository pharmacyRepository;

	@Override
	public MedicineResponse addMedicine(UUID userId, MedicineRequest request) {

		Pharmacy pharmacy = pharmacyRepository.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("Pharmacy profile not found"));

		Medicine medicine = Medicine.builder().pharmacyId(pharmacy.getId()).medicineName(request.getMedicineName())
				.description(request.getDescription()).price(request.getPrice())
				.availableQuantity(request.getAvailableQuantity()).active(true).build();

		Medicine saved = medicineRepository.save(medicine);

		return mapToResponse(saved);
	}

	@Override
	@Transactional(readOnly = true)
	public List<MedicineResponse> getMyMedicines(UUID userId) {

		Pharmacy pharmacy = pharmacyRepository.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("Pharmacy profile not found"));

		return medicineRepository.findByPharmacyIdAndActiveTrue(pharmacy.getId()).stream().map(this::mapToResponse)
				.toList();
	}

	@Override
	public MedicineResponse updateMedicine(UUID userId, UUID medicineId, MedicineRequest request) {

		Pharmacy pharmacy = pharmacyRepository.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("Pharmacy profile not found"));

		Medicine medicine = medicineRepository.findById(medicineId)
				.orElseThrow(() -> new RuntimeException("Medicine not found"));

		if (!medicine.getPharmacyId().equals(pharmacy.getId())) {

			throw new RuntimeException("You can update only your own medicine");
		}

		medicine.setMedicineName(request.getMedicineName());

		medicine.setDescription(request.getDescription());

		medicine.setPrice(request.getPrice());

		medicine.setAvailableQuantity(request.getAvailableQuantity());

		return mapToResponse(medicine);
	}

	@Override
	public void deactivateMedicine(UUID userId, UUID medicineId) {

		Pharmacy pharmacy = pharmacyRepository.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("Pharmacy profile not found"));

		Medicine medicine = medicineRepository.findById(medicineId)
				.orElseThrow(() -> new RuntimeException("Medicine not found"));

		if (!medicine.getPharmacyId().equals(pharmacy.getId())) {

			throw new RuntimeException("You can deactivate only your own medicine");
		}

		medicine.setActive(false);
	}

	private MedicineResponse mapToResponse(Medicine medicine) {

		return MedicineResponse.builder().id(medicine.getId()).pharmacyId(medicine.getPharmacyId())
				.medicineName(medicine.getMedicineName()).description(medicine.getDescription())
				.price(medicine.getPrice()).availableQuantity(medicine.getAvailableQuantity())
				.active(medicine.getActive()).build();
	}

	@Override
	@Transactional(readOnly = true)
	public List<MedicineResponse> searchMedicines(String name) {

		return medicineRepository.searchActiveMedicines(name).stream().map(this::mapToSearchResponse).toList();
	}

	private MedicineResponse mapToSearchResponse(Medicine medicine) {

		Pharmacy pharmacy = pharmacyRepository.findById(medicine.getPharmacyId())
				.orElseThrow(() -> new RuntimeException("Pharmacy not found"));

		return MedicineResponse.builder().id(medicine.getId()).pharmacyId(medicine.getPharmacyId())
				.pharmacyName(pharmacy.getPharmacyName()).medicineName(medicine.getMedicineName())
				.description(medicine.getDescription()).price(medicine.getPrice())
				.availableQuantity(medicine.getAvailableQuantity()).active(medicine.getActive()).build();
	}
}