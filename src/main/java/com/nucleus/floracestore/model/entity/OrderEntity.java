package com.nucleus.floracestore.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "user_name", nullable = false)
    private OrderStatusCodeEntity orderStatusCodeEntity;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_order_placed")
    private Date dateOrderPlaced;

    @Column(name = "order_details")
    private String orderDetails;
}
