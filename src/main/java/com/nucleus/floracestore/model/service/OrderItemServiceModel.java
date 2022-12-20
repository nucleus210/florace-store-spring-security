package com.nucleus.floracestore.model.service;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderItemServiceModel {
    private Long orderItemId;
    private OrderServiceModel order;
    private ProductServiceModel product;
    private OrderItemsStatusCodesServiceModel orderItemStatusCode;
    private Integer orderItemQuantity;
    private BigDecimal orderItemPrice;
    private String rmaNumber;
    private Date rmaIssuedBy;
    private Date rmaIssuedData;
    private String orderItemDetails;
}
