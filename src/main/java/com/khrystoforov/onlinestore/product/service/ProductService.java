package com.khrystoforov.onlinestore.product.service;

import com.khrystoforov.onlinestore.product.dto.request.ProductCreateRequestDto;
import com.khrystoforov.onlinestore.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    Product createProduct(ProductCreateRequestDto dto);

    Page<Product> findAllProducts(Pageable pageable);

    void deleteProduct(UUID id);

    Product getProductByName(String productName);

    void updateProducts(List<Product> products);
}
