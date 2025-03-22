package com.khrystoforov.onlinestore.order.controller;

import com.khrystoforov.onlinestore.order.dto.response.OrderResponseDto;
import com.khrystoforov.onlinestore.order.mapper.OrderMapper;
import com.khrystoforov.onlinestore.order.model.Order;
import com.khrystoforov.onlinestore.order.service.OrderService;
import com.khrystoforov.onlinestore.user.model.User;
import com.khrystoforov.onlinestore.user.service.UserService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    @PostMapping("/add-products")
    public OrderResponseDto addProductToOrder(
            @NotBlank @RequestParam(name = "product") String productName,
            @Min(1) @RequestParam(name = "quantity") Integer quantity
    ) {
        User owner = userService.getCurrentUser();
        Order order = orderService.addProductsToOrder(owner, productName, quantity);
        return OrderMapper.toResponseDto(order);
    }
    @PostMapping("/cancel")
    public void cancelUserOrder(){
        User owner = userService.getCurrentUser();
        orderService.cancelUnpaidUserOrder(owner);
    }
    @GetMapping("/{id}")
    public OrderResponseDto getUserOrderById(@PathVariable UUID id) {
        User owner = userService.getCurrentUser();
        Order order = orderService.getOrderByOwnerAndId(owner, id);
        return OrderMapper.toResponseDto(order);
    }

    @GetMapping
    public List<OrderResponseDto> getAllUserOrders() {
        User owner = userService.getCurrentUser();
        List<Order> orders = orderService.getAllUserOrders(owner);
        return orders.stream().map(OrderMapper::toResponseDto).collect(Collectors.toList());
    }

    @PostMapping("/pay")
    public void payForOrder(){
        User owner = userService.getCurrentUser();
        orderService.payForOrder(owner);
    }
}
