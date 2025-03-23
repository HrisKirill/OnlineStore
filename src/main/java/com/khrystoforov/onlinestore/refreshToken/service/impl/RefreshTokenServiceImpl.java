package com.khrystoforov.onlinestore.refreshToken.service.impl;

import com.khrystoforov.onlinestore.refreshToken.model.RefreshToken;
import com.khrystoforov.onlinestore.refreshToken.repository.RefreshTokenRepository;
import com.khrystoforov.onlinestore.refreshToken.service.RefreshTokenService;
import com.khrystoforov.onlinestore.user.model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken createRefreshToken(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken findByIdAndExpiresAtAfter(UUID refreshToken, LocalDateTime now) {
        return refreshTokenRepository.findByIdAndExpiresAtAfter(refreshToken, now)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Refresh token %s does not found", refreshToken)));
    }

    @Override
    public void revokeUserRefreshToken(User user) {
        refreshTokenRepository.deleteByUserId(user.getId());
    }

}
