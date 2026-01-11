package com.github.sddisk.usernotesbackend.service.auth;

import com.github.sddisk.usernotesbackend.api.dto.auth.AuthResponse;
import com.github.sddisk.usernotesbackend.api.dto.auth.LoginRequestDto;
import com.github.sddisk.usernotesbackend.api.dto.auth.RegisterRequestDto;
import com.github.sddisk.usernotesbackend.api.dto.user.UserMapper;
import com.github.sddisk.usernotesbackend.security.provider.JwtTokenProvider;
import com.github.sddisk.usernotesbackend.service.user.UserService;
import com.github.sddisk.usernotesbackend.store.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequestDto registerDto, HttpServletResponse response) {
        log.info("Register request");

        var user = UserMapper.toUser(registerDto);
        var saved = userService.save(user);

        var accessToken = jwtTokenProvider.generateAccessToken(saved.getEmail());

        // create refresh cookie

        log.info("User successfully registered");
        return new AuthResponse(
                accessToken
        );
    }

    @Override
    public AuthResponse login(LoginRequestDto loginDto, HttpServletResponse response) {
        log.info("Login request");

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.email(),
                        loginDto.password()
                )
        );

        var accessToken = jwtTokenProvider.generateAccessToken(loginDto.email());

        log.info("User successfully login");
        return new AuthResponse(
                accessToken
        );
    }

    @Override
    public void logout(HttpServletResponse response) {
        // todo delete refresh token cookie
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        // mock
        return new AuthResponse(
                "temporary disabled"
        );
    }
}
