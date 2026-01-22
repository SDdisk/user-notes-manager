package com.github.sddisk.usernotesbackend.config;

import com.github.sddisk.usernotesbackend.config.properties.jwt.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableConfigurationProperties(
        value = { JwtProperties.class }
)
@EnableJpaAuditing
public class MainConfiguration {

}