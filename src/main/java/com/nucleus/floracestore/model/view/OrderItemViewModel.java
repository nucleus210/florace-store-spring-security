package com.nucleus.floracestore.model.view;

import com.nucleus.floracestore.model.entity.OrderItemsStatusCodesEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderItemViewModel {
    private Long orderItemId;
    private OrderViewModel order;
    private ProductViewModel product;
    private OrderItemsStatusCodesEntity orderItemStatusCode;
    private Integer orderItemQuantity;
    private BigDecimal orderItemPrice;
    private String rmaNumber;
    private Date rmaIssuedBy;
    private Date rmaIssuedData;
    private String orderItemDetails;
}
