package com.nucleus.floracestore.model.view;

import com.nucleus.floracestore.model.entity.OrderItemEntity;
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
    private Set<OrderItemEntity> orderItems;
    private boolean canDelete;
}
