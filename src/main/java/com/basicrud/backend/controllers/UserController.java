package com.basicrud.backend.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.basicrud.backend.domain.User;
import com.basicrud.backend.dto.UserDetailResponse;
import com.basicrud.backend.services.UserService;


@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailResponse> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        User userEntity = user.get();
        UserDetailResponse response = new UserDetailResponse(
            userEntity.getId(),
            userEntity.getEmail(),
            userEntity.getNickname()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDetailResponse> getCurrentUser(
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        Optional<User> user = userService.getUserById(
            Long.parseLong(userDetails.getUsername())
        );

        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        User userEntity = user.get();
        UserDetailResponse response = new UserDetailResponse(
            userEntity.getId(),
            userEntity.getEmail(),
            userEntity.getNickname()
        );

        return ResponseEntity.ok(response);
    }
}