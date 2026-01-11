package com.github.sddisk.usernotesbackend.api.dto.auth;

public record RegisterRequestDto(
        String username,
        String email,
        String password
) { }