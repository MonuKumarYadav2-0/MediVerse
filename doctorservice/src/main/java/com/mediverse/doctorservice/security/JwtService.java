package com.mediverse.doctorservice.security;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("${jwt.secret}")
	private String secret;

	private SecretKey getSigningKey() {

		return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	public Claims validateToken(String token) {

		return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
	}

	public UUID extractUserId(String token) {

		Claims claims = validateToken(token);

		return UUID.fromString(claims.get("userId", String.class));
	}

	public String extractPhoneNumber(String token) {

		Claims claims = validateToken(token);

		return claims.getSubject();
	}

	public String extractRole(String token) {

		Claims claims = validateToken(token);

		return claims.get("role", String.class);
	}
}