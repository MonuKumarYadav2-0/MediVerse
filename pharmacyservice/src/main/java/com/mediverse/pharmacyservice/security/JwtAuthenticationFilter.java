package com.mediverse.pharmacyservice.security;

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

				// Actual DB userId from JWT
				UUID userId = jwtService.extractUserId(token);

				// Role from JWT
				String role = jwtService.extractRole(token);

				if (userId != null && role != null && SecurityContextHolder.getContext().getAuthentication() == null) {

					UserPrincipal principal = new UserPrincipal(userId, role,
							List.of(new SimpleGrantedAuthority("ROLE_" + role)));

					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							principal, token, principal.getAuthorities());

					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}

		} catch (Exception e) {

			System.out.println("PHARMACY JWT ERROR: " + e.getMessage());

			SecurityContextHolder.clearContext();
		}

		filterChain.doFilter(request, response);
	}
}