package com.nucleus.floracestore.model.dto;

import lombok.Data;

@Data
public class OrderStatusCodesDto {
    private Long orderStatusCodeId;
    private String statusCode;
    private String statusDescription;
}
