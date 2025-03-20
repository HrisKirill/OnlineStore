package com.khrystoforov.onlinestore.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("application")
@RequiredArgsConstructor
@Data
public class ApplicationProperties {
    private final JWTInfo jwt;

    public record JWTInfo(String secret, Long expirationTime) {
    }
}
