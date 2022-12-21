package com.nucleus.floracestore.model.view;

import lombok.Data;

import java.util.Date;

@Data
public class OrderViewModel {
    private Long orderId;
    private String username;
    private OrderStatusCodesViewModel orderStatusCode;
    private Date dateOrderPlaced;
    private String orderDetails;
}
