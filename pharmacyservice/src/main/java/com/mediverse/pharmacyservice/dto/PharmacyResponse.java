package com.mediverse.pharmacyservice.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PharmacyResponse {

    private UUID id;

    private UUID userId;

    private String pharmacyName;

    private String ownerName;

    private String phone;

    private String address;

    private Boolean active;
}