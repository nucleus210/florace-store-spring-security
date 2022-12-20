package com.nucleus.floracestore.model.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "order_item_status_codes")
public class OrderItemsStatusCodesEntity {

    @Id
    @Column(name = "order_item_status_code_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderItemStatusCodeId;

    @Column(name = "order_item_status_code", nullable = false)
    private String productStatus;

    @Column(name = "order_item_status_code_description", columnDefinition = "TEXT", nullable = false)
    private String productStatusDescription;

    @JsonManagedReference(value="order-codes")
    @OneToMany(targetEntity = OrderItemEntity.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "orderItemStatusCode")
    private Set<OrderItemEntity> orderItems;




}
