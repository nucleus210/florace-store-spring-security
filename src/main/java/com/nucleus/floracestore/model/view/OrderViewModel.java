package com.nucleus.floracestore.model.view;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class OrderViewModel {
    private Long orderId;
    private String username;
    private OrderStatusCodesViewModel orderStatusCode;
    private Date dateOrderPlaced;
    private String orderDetails;
    private Set<OrderItemViewModel> orderItems;
}
