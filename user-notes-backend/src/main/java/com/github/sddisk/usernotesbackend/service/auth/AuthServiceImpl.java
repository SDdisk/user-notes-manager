package com.github.sddisk.usernotesbackend.service.auth;

import com.github.sddisk.usernotesbackend.api.dto.auth.AuthResponse;
import com.github.sddisk.usernotesbackend.api.dto.auth.LoginRequestDto;
import com.github.sddisk.usernotesbackend.api.dto.auth.RegisterRequestDto;
import com.github.sddisk.usernotesbackend.api.dto.converter.UserMapper;
import com.github.sddisk.usernotesbackend.security.provider.JwtTokenProvider;
import com.github.sddisk.usernotesbackend.service.kafka.KafkaEmailService;
import com.github.sddisk.usernotesbackend.service.token.RefreshTokenService;
import com.github.sddisk.usernotesbackend.service.user.UserService;
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
    private final RefreshTokenService refreshTokenService;
    private final KafkaEmailService emailService;

    @Override
    public AuthResponse register(RegisterRequestDto registerDto, HttpServletResponse response) {
        log.info("Register request");

        var user = UserMapper.toUser(registerDto);
        var saved = userService.save(user);

        var accessToken = jwtTokenProvider.generateAccessToken(saved.getEmail());
        var refreshToken = jwtTokenProvider.generateRefreshToken(saved.getEmail());

        refreshTokenService.createRefreshTokenCookie(refreshToken, response);

        emailService.sendWelcomeEmail(saved.getEmail(), saved.getUsername());

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
        var refreshToken = jwtTokenProvider.generateRefreshToken(loginDto.email());

        refreshTokenService.createRefreshTokenCookie(refreshToken, response);

        log.info("User successfully login");
        return new AuthResponse(
                accessToken
        );
    }

    @Override
    public void logout(HttpServletResponse response) {
        log.info("Logout request");
        refreshTokenService.deleteRefreshTokenCookie(response);
        log.info("Successfully logout");
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        log.info("Refresh token request");
        var email = jwtTokenProvider.extractEmail(refreshToken);

        var accessToken = jwtTokenProvider.generateAccessToken(email);

        log.info("New access token generated");
        return new AuthResponse(
                accessToken
        );
    }
}
