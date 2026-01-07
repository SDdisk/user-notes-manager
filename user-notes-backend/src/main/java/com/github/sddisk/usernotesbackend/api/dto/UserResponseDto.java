package com.github.sddisk.usernotesbackend.api.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserResponseDto(UUID id, String username, String email) {

}
