package com.mediverse.pharmacyservice.service;

import java.util.List;
import java.util.UUID;

import com.mediverse.pharmacyservice.dto.MedicineRequest;
import com.mediverse.pharmacyservice.dto.MedicineResponse;

public interface MedicineService {

    MedicineResponse addMedicine(
            UUID userId,
            MedicineRequest request
    );

    List<MedicineResponse> getMyMedicines(
            UUID userId
    );

    MedicineResponse updateMedicine(
            UUID userId,
            UUID medicineId,
            MedicineRequest request
    );

    void deactivateMedicine(
            UUID userId,
            UUID medicineId
    );

	List<MedicineResponse> searchMedicines(String name);
}