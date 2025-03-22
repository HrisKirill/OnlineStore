package com.khrystoforov.onlinestore.order.mapper;

import com.khrystoforov.onlinestore.order.dto.response.OrderResponseDto;
import com.khrystoforov.onlinestore.order.model.Order;
import com.khrystoforov.onlinestore.orderItem.mapper.OrderItemMapper;
import com.khrystoforov.onlinestore.user.mapper.UserMapper;

import java.util.stream.Collectors;

public final class OrderMapper {
    public static OrderResponseDto toResponseDto(Order order) {
        return OrderResponseDto.builder()
                .id(order.getId())
                .user(UserMapper.toResponseDto(order.getOwner()))
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .items(order.getOrderItems().stream().map(OrderItemMapper::toResponseDto).collect(Collectors.toList()))
                .total(order.getTotal())
                .build();
    }
}
