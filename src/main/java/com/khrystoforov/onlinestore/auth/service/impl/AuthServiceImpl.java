package com.khrystoforov.onlinestore.auth.service.impl;

import com.khrystoforov.onlinestore.auth.dto.request.LoginRequestDto;
import com.khrystoforov.onlinestore.auth.dto.request.RegisterRequestDto;
import com.khrystoforov.onlinestore.auth.service.AuthService;
import com.khrystoforov.onlinestore.jwt.service.JWTTokenService;
import com.khrystoforov.onlinestore.jwt.dto.response.JWTTokenResponse;
import com.khrystoforov.onlinestore.user.model.Role;
import com.khrystoforov.onlinestore.user.model.User;
import com.khrystoforov.onlinestore.user.service.UserService;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JWTTokenService jwtTokenservice;

    @Override
    public void register(RegisterRequestDto registerRequest) {
        if (userService.existsByEmail(registerRequest.getEmail())) {
            throw new EntityExistsException("Email is already exist!");
        }

        User user = User.builder()
                .email(registerRequest.getEmail())
                .name(registerRequest.getName())
                .role(Role.USER)
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();

        userService.createUser(user);
    }

    @Override
    public JWTTokenResponse login(LoginRequestDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = "Bearer " + jwtTokenservice.generateToken(authentication);
        log.info("logging with [{}]", authentication.getPrincipal());
        return new JWTTokenResponse(jwt);
    }
}
