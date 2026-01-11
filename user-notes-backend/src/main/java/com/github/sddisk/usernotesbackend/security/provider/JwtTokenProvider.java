package com.github.sddisk.usernotesbackend.security.provider;

import java.util.Date;

public interface JwtTokenProvider {
    String generateAccessToken(String email);
    String generateRefreshToken(String email);

    String extractEmail(String token);
    Date extractExpiration(String token);
    boolean isTokenValid(String token);
}