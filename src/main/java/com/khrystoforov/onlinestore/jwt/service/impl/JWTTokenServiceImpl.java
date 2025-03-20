package com.khrystoforov.onlinestore.jwt.service.impl;

import com.khrystoforov.onlinestore.config.ApplicationProperties;
import com.khrystoforov.onlinestore.jwt.service.JWTTokenService;
import com.khrystoforov.onlinestore.user.model.Role;
import com.khrystoforov.onlinestore.user.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class JWTTokenServiceImpl implements JWTTokenService {
    private final ApplicationProperties properties;

    @Override
    public String generateToken(Authentication authentication) {
        ApplicationProperties.JWTInfo jwtInfo = properties.getJwt();
        String secret = jwtInfo.secret();
        User user = (User) authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() + jwtInfo.expirationTime());
        Claims claims = Jwts.claims()
                .subject(user.getEmail())
                .add("userId", user.getId() + "")
                .add("role", user.getRole())
                .add("name", user.getName())
                .build();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSignInKey(secret))
                .compact();
    }


    @Override
    public boolean validateToken(String token) {
        String secret = properties.getJwt().secret();
        try {
            Jwts.parser()
                    .verifyWith(getSignInKey(secret))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        } catch (JwtException ex) {
            log.error(ex.getMessage());
            return false;
        }
    }


    @Override
    public User parseToken(String token) {
        String secret = properties.getJwt().secret();

        try {
            Claims body = Jwts.parser()
                    .verifyWith(getSignInKey(secret))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return User.builder()
                    .email(body.getSubject())
                    .name((String) body.get("name"))
                    .id(Long.parseLong((String) body.get("userId")))
                    .role(Role.getRole((String) body.get("role")))
                    .build();
        } catch (JwtException | ClassCastException e) {
            return null;
        }
    }

    private SecretKey getSignInKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
