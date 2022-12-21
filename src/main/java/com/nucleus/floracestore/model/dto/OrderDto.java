package com.nucleus.floracestore.model.dto;

import com.nucleus.floracestore.model.service.OrderStatusCodesServiceModel;
import lombok.Data;

import java.util.Date;

@Data
public class OrderDto {
    private Long orderId;
    private OrderStatusCodesServiceModel orderStatusCodesEntity;
    private Date dateOrderPlaced;
    private String orderDetails;
}
