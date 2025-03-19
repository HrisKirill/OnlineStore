package com.khrystoforov.onlinestore.product.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequestDto {
    private String name;
    private String description;
    private BigDecimal price;
    private Long stockQuantity;
}
