package com.khrystoforov.onlinestore.jwt.service;

import com.khrystoforov.onlinestore.user.model.User;
import org.springframework.security.core.Authentication;

public interface JWTTokenService {
    String generateToken(Authentication authentication);

    boolean validateToken(String token);

    User parseToken(String token);
}
