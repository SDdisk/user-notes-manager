package com.github.sddisk.usernotesbackend.api.dto.converter;

import com.github.sddisk.usernotesbackend.api.dto.auth.RegisterRequestDto;
import com.github.sddisk.usernotesbackend.api.dto.user.UserResponseDto;
import com.github.sddisk.usernotesbackend.store.entity.User;

public abstract class UserMapper {
    public static UserResponseDto toDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    public static User toUser(RegisterRequestDto dto) {
        return User.builder()
                .username(dto.username())
                .email(dto.email())
                .password(dto.password())
                .build();
    }
}