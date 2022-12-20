package com.nucleus.floracestore.model.dto;

import com.nucleus.floracestore.model.entity.OrderItemEntity;
import com.nucleus.floracestore.model.service.OrderStatusCodesServiceModel;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class OrderDto {
    private Long orderId;
    private OrderStatusCodesServiceModel orderStatusCodesEntity;
    private Date dateOrderPlaced;
    private String orderDetails;
    private Set<OrderItemEntity> orderItems;
}
