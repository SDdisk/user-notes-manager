package com.github.sddisk.usernotesbackend.api.dto.auth;

import jakarta.validation.constraints.*;

public record LoginRequestDto(
        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Incorrect email format")
        String email,

        @NotBlank(message = "Password cannot be empty")
        String password
) {
}