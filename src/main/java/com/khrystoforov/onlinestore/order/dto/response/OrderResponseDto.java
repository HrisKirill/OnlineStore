package com.khrystoforov.onlinestore.order.dto.response;

import com.khrystoforov.onlinestore.order.model.OrderStatus;
import com.khrystoforov.onlinestore.orderItem.dto.response.OrderItemResponseDto;
import com.khrystoforov.onlinestore.user.dto.response.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {
    private UUID id;
    private UserResponseDto user;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderItemResponseDto> items;
    private BigDecimal total;
}
