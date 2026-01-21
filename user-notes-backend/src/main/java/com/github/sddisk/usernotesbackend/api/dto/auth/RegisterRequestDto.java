package com.github.sddisk.usernotesbackend.api.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequestDto(
        @NotBlank(message = "Username cannot be empty")
        String username,

        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Incorrect email format")
        String email,

        @Pattern(
                regexp = "^(?=(.*\\d){5,})(?=(.*[a-zA-Z]){3,})(?=(.*[@#$%^&+=!]){1,})[a-zA-Z\\d@#$%^&+=!]{9,}$",
                message = "Password must contain at least: 5 digits, 3 letters and 1 special character (@#$%^&+=!)"
        )
        String password
) { }