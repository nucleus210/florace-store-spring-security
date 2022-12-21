package com.nucleus.floracestore.model.service;

import lombok.Data;

@Data
public class OrderItemsStatusCodesServiceModel {
    private Long orderItemStatusCodeId;
    private String productStatus;
    private String productStatusDescription;
}
