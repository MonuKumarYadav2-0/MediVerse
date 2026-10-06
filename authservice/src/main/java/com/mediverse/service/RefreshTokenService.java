package com.mediverse.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mediverse.entity.RefreshToken;
import com.mediverse.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;

	@Value("${jwt.refresh-expiration}")
	private long refreshExpiration;

	public RefreshToken createRefreshToken(UUID userId) {

		RefreshToken refreshToken = RefreshToken.builder()
				.token(UUID.randomUUID().toString())
				.userId(userId)
				.expiresAt(Instant.now().plusMillis(refreshExpiration))
				.revoked(false)
				.build();

		return refreshTokenRepository.save(refreshToken);
	}

	public RefreshToken validateRefreshToken(String token) {

		RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
				.orElseThrow(() -> new RuntimeException("Invalid refresh token"));

		if (refreshToken.isRevoked()) {
			throw new RuntimeException("Refresh token has been revoked");
		}

		if (refreshToken.getExpiresAt().isBefore(Instant.now())) {

			throw new RuntimeException("Refresh token has expired");
		}

		return refreshToken;
	}

	public void revokeToken(String token) {

		RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
				.orElseThrow(() -> new RuntimeException("Refresh token not found"));

		refreshToken.setRevoked(true);

		refreshTokenRepository.save(refreshToken);
	}

	public void revokeAllUserTokens(UUID userId) {

		refreshTokenRepository.deleteByUserId(userId);
	}
}