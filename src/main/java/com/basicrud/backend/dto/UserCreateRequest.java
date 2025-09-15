package com.basicrud.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
    @NotBlank(message = "Nickname cannot be blank")
    @Size(min = 3, max = 128, message = "Nickname must be between 3 and 128 characters")
    String nickname,

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    String email,

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 256, message = "Password must be between 8 and 256 characters")
    String password
) {
}
