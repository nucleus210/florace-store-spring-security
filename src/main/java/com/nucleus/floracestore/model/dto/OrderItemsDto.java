package com.nucleus.floracestore.model.dto;

import com.nucleus.floracestore.model.entity.OrderEntity;
import com.nucleus.floracestore.model.entity.OrderItemsStatusCodesEntity;
import com.nucleus.floracestore.model.entity.ProductEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderItemsDto {

    private OrderEntity order;

    private ProductEntity product;

    private OrderItemsStatusCodesEntity orderItemStatusCode;

    private Integer orderItemQuantity;

    private BigDecimal orderItemPrice;

    private String rmaNumber;

    private Date rmaIssuedBy;

    private Date rmaIssuedData;

    private String orderItemDetails;
}
