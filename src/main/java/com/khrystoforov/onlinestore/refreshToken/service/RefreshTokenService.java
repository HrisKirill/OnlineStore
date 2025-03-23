package com.khrystoforov.onlinestore.refreshToken.service;

import com.khrystoforov.onlinestore.refreshToken.model.RefreshToken;
import com.khrystoforov.onlinestore.user.model.User;

import java.time.LocalDateTime;
import java.util.UUID;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(RefreshToken refreshToken);

    RefreshToken findByIdAndExpiresAtAfter(UUID refreshToken, LocalDateTime now);

    void revokeUserRefreshToken(User user);
}
