package org.example.prm392_groupprojectbe.repositories;

import org.example.prm392_groupprojectbe.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
