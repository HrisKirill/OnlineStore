package com.khrystoforov.onlinestore.product.service.impl;

import com.khrystoforov.onlinestore.product.dto.request.ProductCreateRequestDto;
import com.khrystoforov.onlinestore.product.mapper.ProductMapper;
import com.khrystoforov.onlinestore.product.model.Product;
import com.khrystoforov.onlinestore.product.repository.ProductRepository;
import com.khrystoforov.onlinestore.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public Product createProduct(ProductCreateRequestDto dto) {
        Product product = ProductMapper.fromCreateRequestDto(dto);
        return productRepository.save(product);
    }

    @Override
    public Page<Product> findAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }
}
