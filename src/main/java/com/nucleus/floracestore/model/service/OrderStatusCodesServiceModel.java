package com.nucleus.floracestore.model.service;

import lombok.Data;

@Data
public class OrderStatusCodesServiceModel {
    private Long orderStatusCodeId;
    private String statusCode;
    private String statusDescription;
}
