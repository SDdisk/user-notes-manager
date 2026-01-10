package com.github.sddisk.usernotesbackend.config;

import com.github.sddisk.usernotesbackend.jwt.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(
        value = { JwtProperties.class }
)
public class MainConfiguration {

}