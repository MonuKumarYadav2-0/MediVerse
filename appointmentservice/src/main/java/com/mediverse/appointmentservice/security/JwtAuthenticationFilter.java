package com.mediverse.appointmentservice.security;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
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
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader =
                request.getHeader("Authorization");

        // No Authorization header
        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String token =
                authHeader.substring(7);

        try {

            // Validate token and extract claims
            Claims claims =
                    jwtService.extractAllClaims(token);

            UUID userId =
                    UUID.fromString(
                            claims.get("userId", String.class)
                    );

            String phone =
                    claims.get("phone", String.class);

            String role =
                    claims.get("role", String.class);

            // Convert role into Spring authority
            List<SimpleGrantedAuthority> authorities =
                    List.of(
                        new SimpleGrantedAuthority(
                            "ROLE_" + role
                        )
                    );

            // Create our custom principal
            UserPrincipal principal =
                    new UserPrincipal(
                            userId,
                            phone,
                            authorities
                    );

            // Create Authentication object
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            authorities
                    );

            // Store authenticated user
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

        } catch (Exception e) {

            // Invalid/expired JWT
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}