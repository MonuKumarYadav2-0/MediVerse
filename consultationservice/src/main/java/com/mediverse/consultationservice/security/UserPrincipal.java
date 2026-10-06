package com.mediverse.consultationservice.security;

import java.util.UUID;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserPrincipal {

    private final UUID userId;
    private final String role;
}