package com.khrystoforov.onlinestore.auth.service.impl;

import com.khrystoforov.onlinestore.auth.dto.request.LoginRequestDto;
import com.khrystoforov.onlinestore.auth.dto.request.RegisterRequestDto;
import com.khrystoforov.onlinestore.jwt.dto.response.JWTTokenResponse;
import com.khrystoforov.onlinestore.jwt.service.JWTTokenService;
import com.khrystoforov.onlinestore.user.service.UserService;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.khrystoforov.onlinestore.util.EntityUtil.getTestUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserService userService;
    @Mock
    private JWTTokenService jwtTokenservice;
    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequestDto getTestRegisterRequest() {
        return RegisterRequestDto.builder()
                .name("test")
                .email("test@email.com")
                .password("test")
                .build();
    }

    private LoginRequestDto getTestLoginRequest() {
        return LoginRequestDto.builder()
                .email("test@email.com")
                .password("test")
                .build();
    }

    @Test
    void testRegister() {
        when(userService.existsByEmail(getTestRegisterRequest().getEmail())).thenReturn(false);
        when(passwordEncoder.encode(getTestRegisterRequest().getPassword())).thenReturn(getTestUser().getPassword());

        authService.register(getTestRegisterRequest());
        verify(userService, times(1)).createUser(getTestUser());
    }

    @Test
    void testRegisterUserExists() {
        when(userService.existsByEmail(getTestRegisterRequest().getEmail())).thenReturn(true);
        assertThrows(EntityExistsException.class, () -> authService.register(getTestRegisterRequest()));
    }


    @Test
    void testLogin() {
        Authentication authentication = mock(Authentication.class);
        String jwt = "mocked_jwt";
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtTokenservice.generateToken(authentication)).thenReturn(jwt);

        JWTTokenResponse response = authService.login(getTestLoginRequest());

        assertEquals("Bearer " + jwt, response.getToken());
    }
}