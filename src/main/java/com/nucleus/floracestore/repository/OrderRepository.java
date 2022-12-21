package com.nucleus.floracestore.repository;

import com.nucleus.floracestore.model.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("order-repository")
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    OrderEntity save(OrderEntity orderEntity);

    Optional<OrderEntity> findByOrderId(Long orderId);

    @Query("select a from OrderEntity a JOIN a.user u JOIN a.orderStatusCode c where u.username = :username and c.statusCode = :statusCode")
    List<OrderEntity> findAllOrdersByUsernameAndOrderStatusCode(String username, String statusCode);

    @Query("select a from OrderEntity a JOIN FETCH a.user u where u.userId = :userId")
    List<OrderEntity> findAllOrdersByUsername(Long userId);

    @Query("select o from OrderEntity o where o.dateOrderPlaced <= :startDate and o.dateOrderPlaced >= :endDate")
    List<OrderEntity> findByDatePeriod(String startDate, String endDate);

    @Modifying
    @Query("update UserEntity u set u.password = :password where u.userId = :userId")
    void updatePassword(@Param("password") String password, @Param("userId") Long userId);

    @Query("select a from OrderEntity a JOIN a.user u JOIN a.orderStatusCode c where u.username = :username and c.statusCode = :statusCode")
    Optional<OrderEntity> findOrderByUsernameAndOrderStatusCode(String username, String statusCode);
}
