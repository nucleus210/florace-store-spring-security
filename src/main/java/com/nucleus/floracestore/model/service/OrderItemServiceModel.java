package com.nucleus.floracestore.model.service;

import com.nucleus.floracestore.model.entity.OrderItemsStatusCodesEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderItemServiceModel {
    private Long orderItemId;
    private OrderServiceModel order;
    private ProductServiceModel product;
    private OrderItemsStatusCodesEntity orderItemStatusCode;
    private Integer orderItemQuantity;
    private BigDecimal orderItemPrice;
    private String rmaNumber;
    private Date rmaIssuedBy;
    private Date rmaIssuedData;
    private String orderItemDetails;
}
