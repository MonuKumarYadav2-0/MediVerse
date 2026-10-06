package com.mediverse.laboratoryservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LaboratoryRequest {

    @NotBlank
    private String labName;

    @NotBlank
    private String address;

    @NotBlank
    private String phone;
}