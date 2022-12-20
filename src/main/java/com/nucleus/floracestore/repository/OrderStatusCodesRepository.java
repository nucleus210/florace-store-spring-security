package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.OrderStatusCodesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("orderStatusCodeRepository")
public interface OrderStatusCodesRepository extends JpaRepository<OrderStatusCodesEntity, Long> {
    Optional<OrderStatusCodesEntity> findByStatusCode(String code);
}
