package com.mediverse.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.mediverse.entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // Generate JWT
	public String generateToken(User user) {

	    Map<String, Object> claims = new HashMap<>();

	    claims.put("userId", user.getId().toString());
	    claims.put("role", user.getRole().name());

	    return Jwts.builder()
	            .claims(claims)
	            .subject(user.getPhoneNumber())
	            .issuedAt(new Date())
	            .expiration(
	                    new Date(System.currentTimeMillis() + expiration)
	            )
	            .signWith(getSigningKey())
	            .compact();
	}

    // Extract phone number from JWT
    public String extractUsername(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // Check whether token is expired
    public boolean isTokenExpired(String token) {

		return extractExpiration(token).before(new Date());
    }

    // Extract expiration time
    private Date extractExpiration(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }

    // Validate token
    public boolean isTokenValid(String token,UserDetails userDetails) {

        String username = extractUsername(token);

        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}