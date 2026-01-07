package com.github.sddisk.usernotesbackend.api.dto;

import lombok.Builder;

@Builder
public record UserCreateDto(String username, String email, String password) {

}
