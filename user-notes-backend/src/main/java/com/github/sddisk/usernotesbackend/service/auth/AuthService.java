package com.github.sddisk.usernotesbackend.service.auth;

import com.github.sddisk.usernotesbackend.api.dto.auth.AuthResponse;
import com.github.sddisk.usernotesbackend.api.dto.auth.LoginRequestDto;
import com.github.sddisk.usernotesbackend.api.dto.auth.RegisterRequestDto;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    // login, register, logout, refresh
    AuthResponse register(RegisterRequestDto registerDto, HttpServletResponse response);
    AuthResponse login(LoginRequestDto loginDto, HttpServletResponse response);
    void logout(HttpServletResponse response);
    AuthResponse refreshToken(String refreshToken);
}
