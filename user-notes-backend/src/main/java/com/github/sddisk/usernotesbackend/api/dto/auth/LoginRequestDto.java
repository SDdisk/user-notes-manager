package com.github.sddisk.usernotesbackend.api.dto.auth;

public record LoginRequestDto(
        String email,
        String password
) {
}