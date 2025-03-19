package com.khrystoforov.onlinestore.product.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class ProductResponseDto {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private Long stockQuantity;
    private LocalDateTime createdAt;
}
