package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("order-items-repository")
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
    Optional<OrderItemEntity> findByOrderItemId(Long orderId);

    @Query("select o from OrderItemEntity o JOIN o.order a JOIN o.product p where a.orderId = :orderId and p.productId = :productId")
    Optional<OrderItemEntity> findByOrderIdAndProductId(Long orderId, Long productId);

    @Query("select o from OrderItemEntity o JOIN FETCH o.order a where a.orderId = :orderId")
    List<OrderItemEntity> findAllOrderItemsByOrderId(Long orderId);

    @Query("select count(o) from OrderItemEntity o JOIN o.order a where a.orderId = :orderId")
    int findOrderItemsCountByOrderId(Long orderId);
}
