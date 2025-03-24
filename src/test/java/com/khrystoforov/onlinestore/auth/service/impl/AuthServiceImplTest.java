package com.khrystoforov.onlinestore.auth.service.impl;

import com.khrystoforov.onlinestore.auth.dto.request.LoginRequestDto;
import com.khrystoforov.onlinestore.auth.dto.request.RegisterRequestDto;
import com.khrystoforov.onlinestore.auth.dto.response.AuthenticationResponseDto;
import com.khrystoforov.onlinestore.config.ApplicationProperties;
import com.khrystoforov.onlinestore.jwt.service.JWTTokenService;
import com.khrystoforov.onlinestore.refreshToken.model.RefreshToken;
import com.khrystoforov.onlinestore.refreshToken.service.RefreshTokenService;
import com.khrystoforov.onlinestore.user.model.User;
import com.khrystoforov.onlinestore.user.service.UserService;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.khrystoforov.onlinestore.util.EntityUtil.getTestUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
    @Mock
    private ApplicationProperties properties;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private ApplicationProperties.RefreshTokenInfo refreshTokenInfo;
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

    @BeforeEach
    void setUp() {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testRegister() {
        when(userService.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn(getTestUser().getPassword());

        authService.register(getTestRegisterRequest());
        verify(userService, times(1)).createUser(getTestUser());
    }

    @Test
    void testRegisterUserExists() {
        when(userService.existsByEmail(anyString())).thenReturn(true);
        assertThrows(EntityExistsException.class, () -> authService.register(getTestRegisterRequest()));
    }


    @Test
    void testLogin() {
        String jwt = "mocked_jwt";
        UUID refreshTokenId = UUID.randomUUID();
        User user = mock(User.class);
        Authentication authentication = mock(Authentication.class);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(refreshTokenId);

        when(properties.getRefreshToken()).thenReturn(refreshTokenInfo);
        when(refreshTokenInfo.expirationTime()).thenReturn(3600000L);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(jwtTokenservice.generateToken(user)).thenReturn(jwt);
        when(refreshTokenService.createRefreshToken(any(RefreshToken.class))).thenReturn(refreshToken);

        AuthenticationResponseDto response = authService.login(getTestLoginRequest());

        assertEquals(jwt, response.getAccessToken());
        assertEquals(refreshTokenId, response.getRefreshToken());
        verify(refreshTokenService).createRefreshToken(any(RefreshToken.class));
    }

    @Test
    void testRefreshToken() {
        UUID refreshTokenId = UUID.randomUUID();
        String jwt = "mocked_jwt";
        User mockUser = mock(User.class);
        RefreshToken mockRefreshToken = mock(RefreshToken.class);

        when(mockRefreshToken.getUser()).thenReturn(mockUser);
        when(mockRefreshToken.getId()).thenReturn(refreshTokenId);
        when(refreshTokenService.findByIdAndExpiresAtAfter(eq(refreshTokenId), any(LocalDateTime.class)))
                .thenReturn(mockRefreshToken);
        when(jwtTokenservice.generateToken(mockUser)).thenReturn(jwt);

        AuthenticationResponseDto response = authService.refreshToken(refreshTokenId);

        assertEquals(jwt, response.getAccessToken());
        assertEquals(refreshTokenId, response.getRefreshToken());
    }

    @Test
    void revokeUserRefreshToken_ShouldCallServiceMethod() {
        User mockUser = mock(User.class);
        authService.revokeUserRefreshToken(mockUser);
        verify(refreshTokenService).revokeUserRefreshToken(mockUser);
    }
}