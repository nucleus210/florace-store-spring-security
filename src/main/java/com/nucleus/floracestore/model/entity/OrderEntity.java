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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(referencedColumnName = "user_id", name = "user__id")
    private UserEntity user;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(referencedColumnName = "order_status_code_id", name = "order_status_code")
    private OrderStatusCodesEntity orderStatusCode;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_order_placed")
    private Date dateOrderPlaced;

    @Column(name = "order_details")
    private String orderDetails;

}
