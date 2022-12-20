package com.nucleus.floracestore.model.view;

import lombok.Data;

@Data
public class OrderItemsStatusCodesViewModel {
    private Long orderItemStatusCodeId;
    private String productStatus;
    private String productStatusDescription;
}
