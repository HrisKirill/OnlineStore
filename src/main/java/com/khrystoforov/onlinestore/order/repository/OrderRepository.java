package com.khrystoforov.onlinestore.order.repository;

import com.khrystoforov.onlinestore.order.model.Order;
import com.khrystoforov.onlinestore.order.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findOrderByStatusAndOwnerId(OrderStatus status, Long ownerId);

    Optional<Order> findOrderByIdAndOwnerId(UUID id, Long ownerId);

    List<Order> findAllByOwnerId(Long ownerId);

    void deleteByStatusAndOwnerId(OrderStatus status, Long ownerId);

    @Modifying
    @Query("UPDATE Order o SET o.status = :newStatus WHERE o.owner.id = :ownerId AND o.status = :currentStatus")
    void updateUnpaidOrderToPaid(@Param("ownerId") Long ownerId,
                                 @Param("currentStatus") OrderStatus currentStatus,
                                 @Param("newStatus") OrderStatus newStatus);

}
