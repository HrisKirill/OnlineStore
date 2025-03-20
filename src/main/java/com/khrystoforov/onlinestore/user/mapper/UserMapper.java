package com.khrystoforov.onlinestore.user.mapper;

import com.khrystoforov.onlinestore.user.dto.response.UserResponseDto;
import com.khrystoforov.onlinestore.user.model.User;

public final class UserMapper {
    public static UserResponseDto toResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
