package com.khrystoforov.onlinestore.util;

import com.khrystoforov.onlinestore.order.model.Order;
import com.khrystoforov.onlinestore.orderItem.model.OrderItem;
import com.khrystoforov.onlinestore.product.model.Product;
import com.khrystoforov.onlinestore.user.model.Role;
import com.khrystoforov.onlinestore.user.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class EntityUtil {

    public static Product getTestProduct() {
        return Product.builder()
                .id(UUID.randomUUID())
                .name("test")
                .description("test")
                .price(BigDecimal.ONE)
                .stockQuantity(10)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static User getTestUser() {
        return User.builder()
                .email("test@email.com")
                .name("test")
                .password("test")
                .role(Role.USER)
                .build();
    }

    public static Order getTestOrder() {
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(getTestOrderItem());
        return Order.builder()
                .owner(getTestUser())
                .total(BigDecimal.TEN)
                .orderItems(orderItems)
                .build();
    }

    public static OrderItem getTestOrderItem() {
        return OrderItem.builder()
                .product(getTestProduct())
                .quantity(2)
                .build();
    }
}
