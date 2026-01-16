package com.github.sddisk.usernotesbackend.api.controller;

import com.github.sddisk.usernotesbackend.api.dto.auth.AuthResponse;
import com.github.sddisk.usernotesbackend.api.dto.auth.LoginRequestDto;
import com.github.sddisk.usernotesbackend.api.dto.auth.RegisterRequestDto;
import com.github.sddisk.usernotesbackend.service.auth.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse register(@RequestBody @Valid RegisterRequestDto registerRequestDto, HttpServletResponse response) {
        return authService.register(registerRequestDto, response);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse login(@RequestBody @Valid LoginRequestDto loginRequestDto, HttpServletResponse response) {
        return authService.login(loginRequestDto, response);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(HttpServletResponse response) {
        authService.logout(response);
    }

    @PostMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse refreshToken(@CookieValue("refreshToken") String refreshToken) {
        return authService.refreshToken(refreshToken);
    }

}
