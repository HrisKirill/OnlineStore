package com.khrystoforov.onlinestore.orderItem.dto.response;

import com.khrystoforov.onlinestore.product.dto.response.ProductResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponseDto {
    private Long id;
    private ProductResponseDto product;
    private Integer quantity;
}
