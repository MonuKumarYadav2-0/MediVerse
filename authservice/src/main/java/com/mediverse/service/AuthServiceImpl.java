package com.mediverse.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mediverse.dto.AuthResponse;
import com.mediverse.dto.LoginRequest;
import com.mediverse.dto.RegisterRequest;
import com.mediverse.dto.UserResponse;
import com.mediverse.entity.RefreshToken;
import com.mediverse.entity.User;
import com.mediverse.exception.UserNotFoundException;
import com.mediverse.mapper.UserMapper;
import com.mediverse.repository.UserRepository;
import com.mediverse.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final RefreshTokenService refreshTokenService;

	@Override
	public UserResponse register(RegisterRequest request) {

		// Check duplicate phone number
		if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
			throw new RuntimeException("Phone number already registered");
		}

		// Check duplicate email
		if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {

			throw new RuntimeException("Email already registered");
		}

		// Create User
		User user = new User();

		user.setPhoneNumber(request.getPhoneNumber());
		user.setEmail(request.getEmail());

		// Never store plain password
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		user.setRole(request.getRole());

		// Save
		User savedUser = userRepository.save(user);

		// Entity → DTO
		return userMapper.toUserResponse(savedUser);
	}

	@Override
	public AuthResponse login(LoginRequest request) {

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getPhoneNumber(), request.getPassword()));

		

		User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
				.orElseThrow(() -> new RuntimeException("User not found"));

	    // Generate access token
		String accessToken =
		        jwtService.generateToken(user);

	    // Generate refresh token using actual User UUID
	    RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

	    return AuthResponse.builder()
	            .userId(user.getId())
	            .phoneNumber(user.getPhoneNumber())
	            .role(user.getRole().name())
	            .accessToken(accessToken)
	            .refreshToken(refreshToken.getToken())
	            .tokenType("Bearer")
	            .build();
	}
	@Override
	public UserResponse getCurrentUser(String phoneNumber) {

		User user = userRepository.findByPhoneNumber(phoneNumber)
				.orElseThrow(() -> new RuntimeException("User not found"));

		return userMapper.toUserResponse(user);
	}
	
	@Override
	public AuthResponse refreshAccessToken(String token) {

		RefreshToken refreshToken = refreshTokenService.validateRefreshToken(token);

		User user = userRepository.findById(refreshToken.getUserId())
				.orElseThrow(() -> new UserNotFoundException("User not found"));

	    UserDetails userDetails =
	            org.springframework.security.core.userdetails.User
	                    .withUsername(user.getPhoneNumber())
	                    .password(user.getPassword())
	                    .roles(user.getRole().name())
	                    .build();

	    String accessToken =
	            jwtService.generateToken(user);

	    return AuthResponse.builder()
	            .userId(user.getId())
	            .phoneNumber(user.getPhoneNumber())
	            .role(user.getRole().name())
	            .accessToken(accessToken)
	            .refreshToken(refreshToken.getToken())
	            .tokenType("Bearer")
	            .build();
	}
}