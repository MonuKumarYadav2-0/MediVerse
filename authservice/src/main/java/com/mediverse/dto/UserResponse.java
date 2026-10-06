package com.mediverse.dto;

import java.util.UUID;

import com.mediverse.entity.Role;
import com.mediverse.entity.UserStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private UUID userId;
    private String phoneNumber;
    private String email;
    private Role role;
    private UserStatus status;
}