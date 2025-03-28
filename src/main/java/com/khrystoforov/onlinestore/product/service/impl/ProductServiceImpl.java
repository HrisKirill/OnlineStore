package com.khrystoforov.onlinestore.product.service.impl;

import com.khrystoforov.onlinestore.product.dto.request.ProductCreateRequestDto;
import com.khrystoforov.onlinestore.product.mapper.ProductMapper;
import com.khrystoforov.onlinestore.product.model.Product;
import com.khrystoforov.onlinestore.product.repository.ProductRepository;
import com.khrystoforov.onlinestore.product.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public Product createProduct(ProductCreateRequestDto dto) {
        log.info("Create product {}", dto);
        Product product = ProductMapper.fromCreateRequestDto(dto);
        return productRepository.save(product);
    }

    @Override
    public Page<Product> findAllProducts(Pageable pageable) {
        log.info("Find all products");
        return productRepository.findAll(pageable);
    }

    @Override
    public void deleteProduct(UUID id) {
        log.info("Delete product by id {}", id);
        productRepository.deleteById(id);
    }

    @Override
    public Product getProductByName(String productName) {
        log.info("Get product by name {}", productName);
        return productRepository.findByName(productName)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Product is not exist with name %s", productName)));
    }

    @Override
    public void updateProducts(List<Product> products) {
        log.info("Update products");
        productRepository.saveAll(products);
    }
}
