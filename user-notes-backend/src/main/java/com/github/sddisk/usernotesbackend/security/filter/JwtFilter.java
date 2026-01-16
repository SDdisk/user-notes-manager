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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
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
        try {
            extractTokenFromHttpRequest(request).ifPresent(token -> {
                if (jwtTokenProvider.isTokenValid(token) && emptySecurityContext()) {
                    var email = jwtTokenProvider.extractEmail(token);
                    var userDetails = userDetailsService.loadUserByUsername(email);

                    createAuthentication(userDetails, request);
                }
            });

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException | UnsupportedJwtException e) {
            log.error("Handled {} with message: {}", e.getClass().getName(), e.getMessage());
            unauthorizeResponse(response, e);
        }
    }

    private Optional<String> extractTokenFromHttpRequest(HttpServletRequest request) {
        var header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new MalformedJwtException("Authorization must be contain a bearer token");
        }
        
        return Optional.of(header.substring(7));
    }

    private boolean emptySecurityContext() {
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private void createAuthentication(UserDetails userDetails, HttpServletRequest req) {
        var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        var webAuthDetails = new WebAuthenticationDetailsSource().buildDetails(req);

        auth.setDetails(webAuthDetails);

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private void unauthorizeResponse(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        String jsonResponse = "{\"message\": \"" + e.getMessage() +
                "\", \"timeStamp\": \"" + LocalDateTime.now() + "\"}";
        writer.write(jsonResponse);
        writer.flush();

        log.error(
                "Send response {} with status {}",
                jsonResponse,
                HttpStatus.UNAUTHORIZED.value()
        );
    }

}
