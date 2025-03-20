package com.khrystoforov.onlinestore.auth.service;


import com.khrystoforov.onlinestore.auth.dto.request.LoginRequestDto;
import com.khrystoforov.onlinestore.auth.dto.request.RegisterRequestDto;
import com.khrystoforov.onlinestore.jwt.dto.response.JWTTokenResponse;

public interface AuthService {
    void register(RegisterRequestDto registerRequest);

    JWTTokenResponse login(LoginRequestDto loginRequest);
}
