package com.khrystoforov.onlinestore.orderItem.mapper;

import com.khrystoforov.onlinestore.orderItem.dto.response.OrderItemResponseDto;
import com.khrystoforov.onlinestore.orderItem.model.OrderItem;
import com.khrystoforov.onlinestore.product.mapper.ProductMapper;

public final class OrderItemMapper {

    public static OrderItemResponseDto toResponseDto(OrderItem item) {
        return OrderItemResponseDto.builder()
                .id(item.getId())
                .product(ProductMapper.toResponseDto(item.getProduct()))
                .quantity(item.getQuantity())
                .build();
    }
}
