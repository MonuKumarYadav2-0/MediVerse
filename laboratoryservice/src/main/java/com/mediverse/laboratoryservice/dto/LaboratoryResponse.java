package com.mediverse.laboratoryservice.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LaboratoryResponse {

    private UUID id;

    private UUID userId;

    private String labName;

    private String address;

    private String phone;

    private Boolean active;
}