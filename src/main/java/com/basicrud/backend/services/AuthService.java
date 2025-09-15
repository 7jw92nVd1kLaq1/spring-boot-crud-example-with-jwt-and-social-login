package com.basicrud.backend.services;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.basicrud.backend.domain.RefreshToken;
import com.basicrud.backend.domain.User;
import com.basicrud.backend.dto.LoginResponse;
import com.basicrud.backend.dto.UserCreateRequest;
import com.basicrud.backend.repositories.RefreshTokenRepository;
import com.basicrud.backend.repositories.UserRepository;
import com.basicrud.backend.utils.JWTUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final UserService userService;
    private final JWTUtils jwtUtils;

    @Autowired
    public AuthService(
        UserRepository userRepository, 
        RefreshTokenRepository refreshTokenRepository, 
        UserService userService,
        JWTUtils jwtUtils
    ) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    public User registerUser(UserCreateRequest request) {
        return userService.createUser(request);
    }

    public String generateAccessToken(String refreshToken) {
        if (validateRefreshToken(refreshToken) == false) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        Jws<Claims> claims = jwtUtils.getAllClaimsFromToken(refreshToken);
        if (claims == null) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        Date expiration = claims.getPayload().getExpiration();
        if (expiration == null || expiration.before(new Date())) {
            throw new IllegalArgumentException("Refresh token has expired");
        }

        String userId = claims.getPayload().getSubject();

        Date now = new Date();
        return jwtUtils.generateAccessToken(userId, now);
    }

    // Generate both access and refresh tokens
    public LoginResponse generateTokens(Long userId) {
        String refreshToken = generateRefreshToken(userId);
        String accessToken = generateAccessToken(refreshToken);

        return new LoginResponse(accessToken, refreshToken);
    }

    // Add methods to manage refresh tokens as needed
    public String generateRefreshToken(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = userOpt.get();

        Date now = new Date();
        LocalDateTime issuedAt = now.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime();

        RefreshToken refreshToken = new RefreshToken(
            user,
            issuedAt
        );

        refreshTokenRepository.save(refreshToken);
        return jwtUtils.generateRefreshToken(user.getId().toString(), refreshToken.getId(), now);
    }

    public boolean validateAccessToken(String token) {
        // Implementation for validating an access token
        Jws<Claims> claims = jwtUtils.getAllClaimsFromToken(token);
        if (claims == null) {
            return false;
        }

        // Check if the token is expired
        Date expiration = claims.getPayload().getExpiration();
        if (expiration == null || expiration.before(new Date())) {
            return false;
        }

        return true;
    }

    public boolean validateRefreshToken(String token) {
        // Implementation for validating a refresh token
        Jws<Claims> claims = jwtUtils.getAllClaimsFromToken(token);
        if (claims == null) {
            return false;
        }

        // Extract the token ID (jti) from the claims
        String tokenId = claims.getPayload().getId();
        if (tokenId == null || tokenId.isEmpty()) {
            return false;
        }

        UUID tokenUuid;
        try {
            tokenUuid = UUID.fromString(tokenId);
        } catch (IllegalArgumentException e) {
            return false;
        }

        // Check if the token exists in the database
        boolean storedToken = refreshTokenRepository.existsById(tokenUuid);
        if (storedToken == false) {
            return false;
        }

        // Check if the token is expired
        Date expiration = claims.getPayload().getExpiration();
        if (expiration == null || expiration.before(new Date())) {
            return false;
        }

        return true;
    }

    public Jws<Claims> getClaimsFromToken(String token) {
        return jwtUtils.getAllClaimsFromToken(token);
    }

    public void revokeRefreshToken(String token) {
        Jws<Claims> claims = jwtUtils.getAllClaimsFromToken(token);
        if (claims == null) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String tokenId = claims.getPayload().getId();
        if (tokenId == null || tokenId.isEmpty()) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        UUID uuid;
        try {
            uuid = UUID.fromString(tokenId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        refreshTokenRepository.deleteById(uuid);
    }
}
