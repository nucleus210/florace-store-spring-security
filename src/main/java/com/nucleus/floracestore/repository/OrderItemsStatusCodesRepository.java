package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.OrderItemsStatusCodesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("order-items-status-codes-repository")
public interface OrderItemsStatusCodesRepository extends JpaRepository<OrderItemsStatusCodesEntity, Long> {
    Optional<OrderItemsStatusCodesEntity> findByProductStatus(String productStatus);
}
