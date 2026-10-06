package com.mediverse.notificationservice.security;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserPrincipal {

    private UUID userId;

    private String phone;

    private String role;
}