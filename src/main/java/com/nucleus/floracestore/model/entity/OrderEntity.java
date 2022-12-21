package com.nucleus.floracestore.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    @JsonBackReference(value = "order-codes")
    @ManyToOne(targetEntity = OrderStatusCodesEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_status_code_id")
    private OrderStatusCodesEntity orderStatusCode;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_order_placed")
    private Date dateOrderPlaced;

    @Column(name = "order_details")
    private String orderDetails;
    @OneToMany(targetEntity = OrderItemEntity.class,
            cascade = CascadeType.MERGE,
            fetch = FetchType.EAGER,
            mappedBy = "order")
    private Set<OrderItemEntity> orderItems;

}
