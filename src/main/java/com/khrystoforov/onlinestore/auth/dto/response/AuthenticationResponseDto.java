package com.khrystoforov.onlinestore.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthenticationResponseDto {
    private String accessToken;
    private UUID refreshToken;
}
