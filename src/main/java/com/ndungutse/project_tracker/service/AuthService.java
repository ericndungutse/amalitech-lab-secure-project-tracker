package com.ndungutse.project_tracker.service;

import com.ndungutse.project_tracker.dto.LoginRequest;
import com.ndungutse.project_tracker.dto.LoginResponse;
import com.ndungutse.project_tracker.security.CustomUserDetails;
import com.ndungutse.project_tracker.security.JwtUtils;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthService(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()));

            // Get authenticated user details
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // Generate JWT token
            String token = jwtUtils.generateJwtTokenFromUsername(userDetails);

            // Build and return response
            return LoginResponse.builder()
                    .token(token)
                    .userId(userDetails.getUserId())
                    .username(userDetails.getUsername())
                    .email(userDetails.getUser().getEmail())
                    .role(userDetails.getRoleName())
                    .build();

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}