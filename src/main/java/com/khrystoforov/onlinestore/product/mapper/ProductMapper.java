package com.khrystoforov.onlinestore.product.mapper;

import com.khrystoforov.onlinestore.product.dto.request.ProductCreateRequestDto;
import com.khrystoforov.onlinestore.product.dto.response.ProductResponseDto;
import com.khrystoforov.onlinestore.product.model.Product;

public final class ProductMapper {

    public static Product fromCreateRequestDto(ProductCreateRequestDto dto) {
        return Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .stockQuantity(dto.getStockQuantity())
                .build();
    }

    public static ProductResponseDto toResponseDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .createdAt(product.getCreatedAt())
                .build();
    }
}
