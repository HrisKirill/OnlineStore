package com.khrystoforov.onlinestore.order.service.impl;

import com.khrystoforov.onlinestore.exception.ProductQuantityException;
import com.khrystoforov.onlinestore.order.model.Order;
import com.khrystoforov.onlinestore.order.model.OrderStatus;
import com.khrystoforov.onlinestore.order.repository.OrderRepository;
import com.khrystoforov.onlinestore.product.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.khrystoforov.onlinestore.util.EntityUtil.getTestOrder;
import static com.khrystoforov.onlinestore.util.EntityUtil.getTestProduct;
import static com.khrystoforov.onlinestore.util.EntityUtil.getTestUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductService productService;
    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void testAddProductsToOrderIfNotExists() {
        Order sourceOrder = Order.builder()
                .owner(getTestUser())
                .total(BigDecimal.ZERO)
                .orderItems(getTestOrder().getOrderItems())
                .build();

        int quantity = 10;
        when(productService.getProductByName(getTestProduct().getName())).thenReturn(getTestProduct());
        when(orderRepository.findOrderByStatusAndOwnerId(OrderStatus.UNPAID, getTestUser().getId()))
                .thenReturn(Optional.of(sourceOrder));
        when(orderRepository.save(getTestOrder())).thenReturn(getTestOrder());
        Order order = orderService.addProductsToOrder(getTestUser(), getTestProduct().getName(), quantity);
        assertEquals(BigDecimal.valueOf(quantity), order.getTotal());
        assertEquals(1, order.getOrderItems().size());
        verify(orderRepository, times(1)).save(getTestOrder());
    }

    @Test
    void testAddProductsToOrderThrowsProductQuantityException() {
        when(productService.getProductByName(getTestProduct().getName())).thenReturn(getTestProduct());

        assertThrows(ProductQuantityException.class,
                () -> orderService.addProductsToOrder(getTestUser(), getTestProduct().getName(), 100_000_000));

    }

    @Test
    void testGetOrderByStatusAndOwnerOrCreateIfNotExistReturnsExistingOrder() {
        when(orderRepository.findOrderByStatusAndOwnerId(OrderStatus.UNPAID, getTestUser().getId()))
                .thenReturn(Optional.of(getTestOrder()));

        Order order = orderService.getOrderByStatusAndOwnerOrCreateIfNotExist(OrderStatus.UNPAID, getTestUser());

        assertEquals(getTestOrder().getId(), order.getId());
        verify(orderRepository, never()).save(getTestOrder());
    }

    @Test
    void testGetOrderByStatusAndOwnerOrCreateIfNotExistCreatesNewOrder() {
        Order expectedOrder = new Order();
        when(orderRepository.findOrderByStatusAndOwnerId(OrderStatus.UNPAID, getTestUser().getId()))
                .thenReturn(Optional.empty());
        when(orderRepository.save(any(Order.class))).thenReturn(expectedOrder);
        orderService.getOrderByStatusAndOwnerOrCreateIfNotExist(OrderStatus.UNPAID, getTestUser());

        verify(orderRepository, times(1)).save(expectedOrder);
    }

    @Test
    void testGetOrderByOwnerAndId() {
        when(orderRepository.findOrderByIdAndOwnerId(getTestOrder().getId(), getTestUser().getId()))
                .thenReturn(Optional.of(getTestOrder()));

        Order order = orderService.getOrderByOwnerAndId(getTestUser(), getTestOrder().getId());

        assertEquals(getTestOrder().getId(), order.getId());
    }

    @Test
    void testGetOrderByOwnerAndIdThrowsEntityNotFoundException() {
        when(orderRepository.findOrderByIdAndOwnerId(getTestOrder().getId(), getTestUser().getId()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> orderService.getOrderByOwnerAndId(getTestUser(), getTestOrder().getId()));
    }

    @Test
    void testGetAllUserOrders() {
        List<Order> orders = List.of(getTestOrder());
        when(orderRepository.findAllByOwnerId(getTestUser().getId())).thenReturn(orders);

        List<Order> result = orderService.getAllUserOrders(getTestUser());

        assertEquals(1, result.size());
        assertEquals(getTestOrder(), result.get(0));
    }


    @Test
    void testPayForOrder() {
        orderService.payForOrder(getTestUser());

        verify(orderRepository, times(1))
                .updateUnpaidOrderToPaid(getTestUser().getId(), OrderStatus.UNPAID, OrderStatus.PAID);
    }
}