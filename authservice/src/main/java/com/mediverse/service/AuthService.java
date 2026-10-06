package com.mediverse.service;

import com.mediverse.dto.AuthResponse;
import com.mediverse.dto.LoginRequest;
import com.mediverse.dto.RegisterRequest;
import com.mediverse.dto.UserResponse;

public interface AuthService {

    UserResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

	UserResponse getCurrentUser(String phoneNumber);

	AuthResponse refreshAccessToken(String token);
}