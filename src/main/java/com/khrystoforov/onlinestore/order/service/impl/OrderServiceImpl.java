package com.khrystoforov.onlinestore.order.service.impl;

import com.khrystoforov.onlinestore.exception.ProductQuantityException;
import com.khrystoforov.onlinestore.order.model.Order;
import com.khrystoforov.onlinestore.order.model.OrderStatus;
import com.khrystoforov.onlinestore.order.repository.OrderRepository;
import com.khrystoforov.onlinestore.order.service.OrderService;
import com.khrystoforov.onlinestore.orderItem.model.OrderItem;
import com.khrystoforov.onlinestore.product.model.Product;
import com.khrystoforov.onlinestore.product.service.ProductService;
import com.khrystoforov.onlinestore.user.model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;

    @Override
    public Order addProductsToOrder(User owner, String productName, Integer quantity) {
        Product product = productService.getProductByName(productName);
        if (product.getStockQuantity() < quantity) {
            throw new ProductQuantityException(String.format("The number of products [%d] in stock is less than in the request [%d]",
                    product.getStockQuantity(), quantity));
        }

        Integer oldQuantity = product.getStockQuantity();
        product.setStockQuantity(oldQuantity - quantity);

        Order order = getOrderByStatusAndOwnerOrCreateIfNotExist(OrderStatus.UNPAID, owner);
        BigDecimal oldTotal = order.getTotal();
        BigDecimal newTotal = oldTotal.add(product.getPrice().multiply(new BigDecimal(quantity)));
        order.setTotal(newTotal);
        Optional<OrderItem> itemOptional = findOrderItemWithProduct(order, product);

        OrderItem orderItem;
        if (itemOptional.isPresent()) {
            orderItem = itemOptional.get();
            Integer oldItemQuantity = orderItem.getQuantity();
            orderItem.setQuantity(oldItemQuantity + quantity);
        } else {
            orderItem = OrderItem.builder()
                    .product(product)
                    .order(order)
                    .quantity(quantity)
                    .build();

            order.addOrderItem(orderItem);
        }

        return orderRepository.save(order);
    }

    public Optional<OrderItem> findOrderItemWithProduct(Order order, Product product) {
        List<OrderItem> itemList = order.getOrderItems();
        for (OrderItem item :
                itemList) {
            if (product.equals(item.getProduct())) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    @Override
    public Order getOrderByStatusAndOwnerOrCreateIfNotExist(OrderStatus status, User owner) {
        return orderRepository.findOrderByStatusAndOwnerId(status, owner.getId())
                .orElseGet(() -> orderRepository.save(new Order(owner)));
    }

    @Override
    public Order getOrderByOwnerAndId(User owner, UUID id) {
        return orderRepository.findOrderByIdAndOwnerId(id, owner.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Order is not exist with id %s for user %d", id, owner.getId())));
    }

    @Override
    public List<Order> getAllUserOrders(User owner) {
        return orderRepository.findAllByOwnerId(owner.getId());
    }

    @Override
    @Transactional
    public void cancelUnpaidUserOrder(User owner) {
        Order order = orderRepository.findOrderByStatusAndOwnerId(OrderStatus.UNPAID, owner.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Order not found for user %d", owner.getId())));
        List<Product> products = new ArrayList<>();
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem item :
                orderItems) {
            Integer quantity = item.getQuantity();
            Product product = item.getProduct();
            Integer currentProductQuantity = product.getStockQuantity();
            product.setStockQuantity(currentProductQuantity + quantity);
            products.add(product);
        }

        productService.updateProducts(products);
        orderRepository.deleteByStatusAndOwnerId(OrderStatus.UNPAID, owner.getId());
    }


    @Override
    @Transactional
    public void payForOrder(User owner) {
        orderRepository.updateUnpaidOrderToPaid(owner.getId(), OrderStatus.UNPAID, OrderStatus.PAID);
    }

}
