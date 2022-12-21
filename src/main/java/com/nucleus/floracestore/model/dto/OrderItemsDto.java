package com.nucleus.floracestore.model.dto;

import com.nucleus.floracestore.model.service.OrderItemsStatusCodesServiceModel;
import com.nucleus.floracestore.model.service.OrderServiceModel;
import com.nucleus.floracestore.model.service.ProductServiceModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderItemsDto {
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
