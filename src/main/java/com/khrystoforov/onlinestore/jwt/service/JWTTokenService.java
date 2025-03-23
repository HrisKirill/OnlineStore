package com.khrystoforov.onlinestore.jwt.service;

import com.khrystoforov.onlinestore.user.model.User;

public interface JWTTokenService {
    String generateToken(User user);

    boolean validateToken(String token);

    User parseToken(String token);
}
