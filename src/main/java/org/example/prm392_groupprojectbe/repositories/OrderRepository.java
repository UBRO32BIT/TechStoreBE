package org.example.prm392_groupprojectbe.repositories;

import org.example.prm392_groupprojectbe.entities.Order;
import org.example.prm392_groupprojectbe.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE " +
            "(:status IS NULL OR o.status = :status) AND " +
            "(:startDate IS NULL OR o.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR o.createdAt <= :endDate)")
    Page<Order> findByParameters(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("status") OrderStatus status,
            Pageable pageable
    );
}
