package com.khrystoforov.onlinestore.refreshToken.repository;

import com.khrystoforov.onlinestore.refreshToken.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByIdAndExpiresAtAfter(UUID id, LocalDateTime date);

    void deleteByUserId(Long userId);
}
