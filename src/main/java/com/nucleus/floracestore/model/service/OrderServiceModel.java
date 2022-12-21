package com.nucleus.floracestore.model.service;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class OrderServiceModel {
    private Long orderId;
    private UserServiceModel user;
    private OrderStatusCodesServiceModel orderStatusCode;
    private Date dateOrderPlaced;
    private String orderDetails;
    private Set<OrderItemServiceModel> orderItems;

}
