package com.khrystoforov.onlinestore.auth.service;


import com.khrystoforov.onlinestore.auth.dto.request.LoginRequestDto;
import com.khrystoforov.onlinestore.auth.dto.request.RegisterRequestDto;
import com.khrystoforov.onlinestore.auth.dto.response.AuthenticationResponseDto;
import com.khrystoforov.onlinestore.user.model.User;

import java.util.UUID;

public interface AuthService {
    void register(RegisterRequestDto registerRequest);

    AuthenticationResponseDto login(LoginRequestDto loginRequest);

    AuthenticationResponseDto refreshToken(UUID tokenId);

    void revokeUserRefreshToken(User user);
}
