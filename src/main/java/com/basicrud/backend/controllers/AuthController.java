package com.basicrud.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.basicrud.backend.domain.User;
import com.basicrud.backend.dto.LoginRequest;
import com.basicrud.backend.dto.LoginResponse;
import com.basicrud.backend.dto.RefreshRequest;
import com.basicrud.backend.dto.RefreshResponse;
import com.basicrud.backend.dto.UserCreateRequest;
import com.basicrud.backend.dto.UserCreatedResponse;
import com.basicrud.backend.services.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    @Autowired
    public AuthController(
        AuthenticationManager authenticationManager,
        AuthService authService
    ) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
    }
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.email(),
                loginRequest.password()
            )
        );
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        LoginResponse response = authService.generateTokens(Long.parseLong(userDetails.getUsername()));

        return ResponseEntity.ok(response);
    }


    @PostMapping("/register")
    public ResponseEntity<UserCreatedResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        User newUser = authService.registerUser(request);
        UserCreatedResponse response = new UserCreatedResponse(
            newUser.getId()
        );
        return ResponseEntity.ok(response);
    }


    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refreshToken(
        @RequestBody RefreshRequest refreshRequest
    ) {
        String accessToken = authService.generateAccessToken(refreshRequest.refreshToken());
        RefreshResponse response = new RefreshResponse(accessToken);
        return ResponseEntity.ok(response);
    }
}
