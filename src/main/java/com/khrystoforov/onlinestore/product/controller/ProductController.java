package com.khrystoforov.onlinestore.product.controller;

import com.khrystoforov.onlinestore.product.dto.request.ProductCreateRequestDto;
import com.khrystoforov.onlinestore.product.dto.response.ProductResponseDto;
import com.khrystoforov.onlinestore.product.mapper.ProductMapper;
import com.khrystoforov.onlinestore.product.model.Product;
import com.khrystoforov.onlinestore.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/products")
@Validated
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ProductResponseDto createProduct(@Valid @RequestBody ProductCreateRequestDto dto) {
        return ProductMapper.toResponseDto(productService.createProduct(dto));
    }
    @GetMapping
    public Page<ProductResponseDto> getProducts(Pageable pageable){
        Page<Product> products = productService.findAllProducts(pageable);
        return products.map(ProductMapper::toResponseDto);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable UUID id){
        productService.deleteProduct(id);
    }
}
