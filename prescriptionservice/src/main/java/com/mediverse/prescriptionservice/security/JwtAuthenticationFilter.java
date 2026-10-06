package com.mediverse.prescriptionservice.security;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authorization = request.getHeader("Authorization");

		if (authorization == null || !authorization.startsWith("Bearer ")) {

			filterChain.doFilter(request, response);
			return;
		}

		String token = authorization.substring(7);

		try {

			if (jwtService.isTokenValid(token)) {

				// Actual DB userId from JWT userId claim
				UUID userId = jwtService.extractUserId(token);

				// Phone number from JWT subject
				String username = jwtService.extractUsername(token);

				// DOCTOR / PATIENT
				String role = jwtService.extractRole(token);

				UserPrincipal principal = new UserPrincipal(userId, username,
						List.of(new SimpleGrantedAuthority("ROLE_" + role)));

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal,
						null, principal.getAuthorities());

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}

		} catch (Exception e) {

			System.out.println("JWT ERROR: " + e.getMessage());

			e.printStackTrace();

			SecurityContextHolder.clearContext();
		}

		filterChain.doFilter(request, response);
	}
}