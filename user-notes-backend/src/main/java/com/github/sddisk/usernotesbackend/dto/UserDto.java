package com.github.sddisk.usernotesbackend.dto;

import java.util.UUID;

public record UserDto(UUID id, String username, String email, String password) {
}