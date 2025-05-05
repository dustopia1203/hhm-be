package com.hhm.api.repository;

import com.hhm.api.model.entity.OrderItem;
import com.hhm.api.repository.custom.OrderItemRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID>, OrderItemRepositoryCustom {
}
