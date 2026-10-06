package com.mediverse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class AuthResponse {

    private UUID userId;
    private String phoneNumber;
    private String role;
    private String accessToken;
    private String tokenType;
    private String refreshToken;
}