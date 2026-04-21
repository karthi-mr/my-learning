package com.learn.backend.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.jwt")
@Getter
@Setter
public class SecurityConfigProperties {

    private String secret;

    private long accessExpiration;

    private long refreshExpiration;
}
