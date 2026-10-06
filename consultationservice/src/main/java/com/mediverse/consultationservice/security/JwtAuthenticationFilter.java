package com.mediverse.consultationservice.security;

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
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("=== CONSULTATION JWT FILTER ===");

        String authorization =
                request.getHeader("Authorization");

        System.out.println(
                "Authorization = " + authorization
        );

        if (authorization == null
                || !authorization.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(7);

        try {

            // 1. Validate JWT first
            if (!jwtService.isTokenValid(token)) {

                System.out.println("JWT IS INVALID");

                filterChain.doFilter(request, response);
                return;
            }

            // 2. Extract actual DB userId
            UUID userId =
                    jwtService.extractUserId(token);

            // 3. Extract role
            String role =
                    jwtService.extractRole(token);

            System.out.println(
                    "USER ID = " + userId
            );

            System.out.println(
                    "ROLE = " + role
            );

            if (userId != null
                    && role != null
                    && SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null) {

                UserPrincipal principal =
                        new UserPrincipal(
                                userId,
                                role
                        );

                SimpleGrantedAuthority authority =
                        new SimpleGrantedAuthority(
                                "ROLE_" + role
                        );

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                principal,
                                token,
                                List.of(authority)
                        );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(
                                authentication
                        );
            }

        } catch (Exception e) {

            System.out.println(
                    "CONSULTATION JWT ERROR = "
                            + e.getMessage()
            );

            e.printStackTrace();

            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}