package com.nucleus.floracestore.model.entity;


import com.nucleus.floracestore.model.enums.ProductStatusEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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
}
