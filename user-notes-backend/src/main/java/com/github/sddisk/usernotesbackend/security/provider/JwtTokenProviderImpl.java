package com.github.sddisk.usernotesbackend.security.provider;

import com.github.sddisk.usernotesbackend.config.properties.jwt.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.lang.Function;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

import static java.lang.System.currentTimeMillis;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProviderImpl implements JwtTokenProvider {
    private final JwtProperties jwtProperties;

    @Getter @Setter
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(
                jwtProperties.getSecretKey().getBytes()
        );
    }

    private Date createAccessExpirationTime() {
        return new Date(jwtProperties.getAccessLifetime() + currentTimeMillis());
    }

    private Date createRefreshExpirationTime() {
        return new Date(jwtProperties.getRefreshLifetime() + currentTimeMillis());
    }

    private Claims parseClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private <T> T getClaim(String token, Function<Claims, T> claimsFunction) {
        return claimsFunction.apply(parseClaims(token));
    }

    private <T> T getClaim(Claims claims, Function<Claims, T> claimsFunction) {
        return claimsFunction.apply(claims);
    }

    private String getSubject(String token) {
        return getClaim(token, Claims::getSubject);
    }

    private String getSubject(Claims claims) {
        return getClaim(claims, Claims::getSubject);
    }

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private Date getExpiration(Claims claims) {
        return getClaim(claims, Claims::getExpiration);
    }

    private String buildToken(String email, Date expiration) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    @Override
    public String generateAccessToken(String email) {
        return buildToken(email, createAccessExpirationTime());
    }

    @Override
    public String generateRefreshToken(String email) {
        return buildToken(email, createRefreshExpirationTime());
    }

    @Override
    public boolean isTokenValid(String token) {
        if (token == null || token.trim().isEmpty()) return false;
        try {
            // parse once
            var claims = parseClaims(token);

            if (getExpiration(claims).before(new Date())) return false;

            String email = getSubject(claims);
            if (email == null || email.isEmpty()) return false;

            return true;
        } catch (ExpiredJwtException e) {
            log.info("Token expired: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            log.warn("Invalid token format: {}", e.getMessage());
            return false;
        } catch (SignatureException e) {
            log.warn("Invalid signature: {}", e.getMessage());
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid token: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public String extractEmail(String token) {
        return getSubject(token);
    }

    @Override
    public Date extractExpiration(String token) {
        return getExpiration(token);
    }
}
