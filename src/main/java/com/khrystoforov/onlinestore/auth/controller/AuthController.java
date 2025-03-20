package com.khrystoforov.onlinestore.auth.controller;

import com.khrystoforov.onlinestore.auth.dto.request.LoginRequestDto;
import com.khrystoforov.onlinestore.auth.dto.request.RegisterRequestDto;
import com.khrystoforov.onlinestore.auth.service.AuthService;
import com.khrystoforov.onlinestore.jwt.dto.response.JWTTokenResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/register")
    public void register(@RequestBody RegisterRequestDto registerRequest) {
        authService.register(registerRequest);
    }
    @PostMapping("/login")
    public JWTTokenResponse login(@RequestBody LoginRequestDto loginRequest) {
        return authService.login(loginRequest);
    }
}
