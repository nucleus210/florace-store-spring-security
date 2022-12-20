package com.nucleus.floracestore.model.view;

import lombok.Data;

@Data
public class OrderStatusCodesViewModel {
    private Long orderStatusCodeId;
    private String statusCode;
    private String statusDescription;
}
