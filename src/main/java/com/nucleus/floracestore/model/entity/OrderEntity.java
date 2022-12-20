package com.nucleus.floracestore.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(referencedColumnName = "user_id", name = "user__id")
    private UserEntity user;
    @JsonBackReference(value="order-codes")
    @ManyToOne(targetEntity=OrderStatusCodesEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name="order_status_code_id")
    private OrderStatusCodesEntity orderStatusCode;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_order_placed")
    private Date dateOrderPlaced;

    @Column(name = "order_details")
    private String orderDetails;
    @JsonManagedReference(value = "order-items")
    @OneToMany(targetEntity = OrderItemEntity.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "order")
    private Set<OrderItemEntity> orderItems;

}
