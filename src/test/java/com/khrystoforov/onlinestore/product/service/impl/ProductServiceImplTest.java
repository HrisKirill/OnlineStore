package com.khrystoforov.onlinestore.product.service.impl;

import com.khrystoforov.onlinestore.product.dto.request.ProductCreateRequestDto;
import com.khrystoforov.onlinestore.product.mapper.ProductMapper;
import com.khrystoforov.onlinestore.product.model.Product;
import com.khrystoforov.onlinestore.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.khrystoforov.onlinestore.util.EntityUtil.getTestProduct;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    private ProductRepository repository;
    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void testCreateProduct() {
        ProductCreateRequestDto dto = ProductCreateRequestDto.builder()
                .name("test")
                .description("test")
                .price(BigDecimal.ONE)
                .stockQuantity(1)
                .build();
        Product product = ProductMapper.fromCreateRequestDto(dto);
        when(repository.save(product))
                .thenReturn(getTestProduct());

        Product result = productService.createProduct(dto);
        assertEquals(getTestProduct().getName(), result.getName());
        assertEquals(getTestProduct().getDescription(), result.getDescription());
        assertEquals(getTestProduct().getPrice(), result.getPrice());
        assertEquals(getTestProduct().getStockQuantity(), result.getStockQuantity());
    }

    @Test
    void testFindAllProducts() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        List<Product> productList = List.of(getTestProduct(), getTestProduct());
        Page<Product> productPage = new PageImpl<>(productList, pageable, productList.size());
        when(repository.findAll(pageable)).thenReturn(productPage);
        Page<Product> result = productService.findAllProducts(pageable);

        verify(repository, times(1)).findAll(pageable);
        assertEquals(2, result.getContent().size());
    }

    @Test
    void testDeleteProduct() {
        UUID id = getTestProduct().getId();
        productService.deleteProduct(id);
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void testGetProductByName() {
        when(repository.findByName(anyString())).thenReturn(Optional.of(getTestProduct()));
        Product result = productService.getProductByName(getTestProduct().getName());

        verify(repository, times(1)).findByName(getTestProduct().getName());
        assertEquals(getTestProduct().getName(), result.getName());
    }

    @Test
    void testGetProductByNameThrowsEntityNotFoundException() {
        String nonExistent = "NonExistent";
        when(repository.findByName(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                productService.getProductByName(nonExistent));
        verify(repository, times(1)).findByName(nonExistent);
    }

    @Test
    void testUpdateProducts() {
        List<Product> products = List.of(getTestProduct());
        productService.updateProducts(products);
        verify(repository, times(1)).saveAll(products);
    }
}