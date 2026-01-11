package com.github.sddisk.usernotesbackend.security.filter;

import com.github.sddisk.usernotesbackend.security.provider.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.WebConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        extractTokenFromHttpRequest(request).ifPresent(token -> {
            if (jwtTokenProvider.isTokenValid(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
                var email = jwtTokenProvider.extractEmail(token);
                var userDetails = userDetailsService.loadUserByUsername(email);

                createAuthentication(userDetails, request);
            } else {
                unauthorizeResponse(response);
            }
        });

        filterChain.doFilter(request, response);
    }

    private Optional<String> extractTokenFromHttpRequest(HttpServletRequest request) {
        var header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) return Optional.empty();

        return Optional.of(header.substring(7));
    }

    private void createAuthentication(UserDetails userDetails, HttpServletRequest req) {
        var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        var webAuthDetails = new WebAuthenticationDetailsSource().buildDetails(req);

        auth.setDetails(webAuthDetails);

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private void unauthorizeResponse(HttpServletResponse response) {
        try {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            String message = "Bad authorization token";
            String jsonResponse = "{\"message\": \"" + message +
                    "\", \"timeStamp\": \"" + System.currentTimeMillis() + "\"}";
            writer.write(jsonResponse);
            writer.flush();

            log.error(
                    "Send response {} with status {}",
                    jsonResponse,
                    HttpStatus.UNAUTHORIZED.value()
            );
        } catch (IOException e) {
            log.error(
                    "Caught exception {} with message: {}",
                    e.getClass().getSimpleName(),
                    e.getMessage()
            );
        }
    }
}
