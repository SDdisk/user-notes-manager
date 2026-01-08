package com.github.sddisk.usernotesbackend.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("jwt")
@Getter @Setter
public class JwtProperties {
    private String secretKey;
    private String accessLifetime;
    private String refreshLifetime;
}