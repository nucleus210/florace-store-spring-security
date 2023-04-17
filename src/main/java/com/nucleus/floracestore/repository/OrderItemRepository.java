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

    @Query("select o from OrderItemEntity o JOIN FETCH o.order a where a.orderId = :orderId")
    List<OrderItemEntity> findAllOrderItemsByOrderId(Long orderId);

    @Query("select count(o) from OrderItemEntity o JOIN o.order a where a.orderId = :orderId")
    int findOrderItemsCountByOrderId(Long orderId);

    @Query("select i from OrderItemEntity i JOIN i.order o JOIN i.product p where o.orderId = :orderId and p.productId = :productId")
    Optional<OrderItemEntity> findOrderItemsByOrderIdAndProductId(Long orderId, Long productId);
}
