package com.khrystoforov.onlinestore.order.service;

import com.khrystoforov.onlinestore.order.model.Order;
import com.khrystoforov.onlinestore.order.model.OrderStatus;
import com.khrystoforov.onlinestore.user.model.User;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    Order addProductsToOrder(User owner, String productName, Integer quantity);

    Order getOrderByStatusAndOwnerOrCreateIfNotExist(OrderStatus status, User owner);

    Order getOrderByOwnerAndId(User owner, UUID id);

    List<Order> getAllUserOrders(User owner);

    void cancelUnpaidUserOrder(User owner);

    void payForOrder(User owner);
}
