package com.lifepattern.ai.service;

import com.lifepattern.ai.dto.*;
import com.lifepattern.ai.entity.User;
import com.lifepattern.ai.exception.BadRequestException;
import com.lifepattern.ai.repository.UserRepository;
import com.lifepattern.ai.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        
        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .build();
        
        userRepository.save(user);
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        var jwtToken = jwtService.generateToken(userDetails);
        
        return AuthResponse.builder()
                .access_token(jwtToken)
                .user(UserResponse.builder()
                        .id(user.getId().toString())
                        .email(user.getEmail())
                        .name(user.getName())
                        .build())
                .build();
    }
    
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("User not found"));
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        var jwtToken = jwtService.generateToken(userDetails);
        
        return AuthResponse.builder()
                .access_token(jwtToken)
                .user(UserResponse.builder()
                        .id(user.getId().toString())
                        .email(user.getEmail())
                        .name(user.getName())
                        .build())
                .build();
    }
    
    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found"));
        
        return UserResponse.builder()
                .id(user.getId().toString())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
    
    public void forgotPassword(ForgotPasswordRequest request) {
        // Mock implementation - in production, you would send an email with reset link
        var user = userRepository.findByEmail(request.getEmail());
        if (user.isEmpty()) {
            // Don't reveal if email exists or not for security
            return;
        }
        // Log or send email here
        System.out.println("Password reset requested for: " + request.getEmail());
    }

    @Transactional
    public UserResponse updateProfile(String currentEmail, UpdateProfileRequest request) {
        var user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new BadRequestException("User not found"));
        
        // Check if new email already exists (if email is being changed)
        if (!user.getEmail().equals(request.getEmail())) {
                if (userRepository.existsByEmail(request.getEmail())) {
                throw new BadRequestException("Email already exists");
                }
                user.setEmail(request.getEmail());
        }
        
        // Update name
        user.setName(request.getName());
        
        userRepository.save(user);
        
        return UserResponse.builder()
                .id(user.getId().toString())
                .email(user.getEmail())
                .name(user.getName())
                .build();
     }
}
