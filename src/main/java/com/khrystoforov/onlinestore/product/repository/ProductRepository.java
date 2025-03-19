package com.khrystoforov.onlinestore.product.repository;

import com.khrystoforov.onlinestore.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
