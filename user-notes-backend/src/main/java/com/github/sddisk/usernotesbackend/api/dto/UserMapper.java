package com.github.sddisk.usernotesbackend.api.dto;

import com.github.sddisk.usernotesbackend.store.entity.User;

public abstract class UserMapper {
    public static UserResponseDto toDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}