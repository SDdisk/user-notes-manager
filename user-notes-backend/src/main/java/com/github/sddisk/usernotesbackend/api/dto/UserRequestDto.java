package com.github.sddisk.usernotesbackend.api.dto;

import lombok.Builder;

@Builder
public record UserRequestDto(String username, String email, String password) {

}
