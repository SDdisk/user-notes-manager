package com.github.sddisk.usernotesbackend.service.token;

import jakarta.servlet.http.HttpServletResponse;

public interface RefreshTokenService {
    void createRefreshTokenCookie(String token, HttpServletResponse response);
    void deleteRefreshTokenCookie(HttpServletResponse response);
}
