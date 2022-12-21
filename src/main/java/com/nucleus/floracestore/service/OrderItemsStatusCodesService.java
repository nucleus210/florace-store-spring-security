package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.enums.ProductStatusEnum;
import com.nucleus.floracestore.model.service.OrderItemsStatusCodesServiceModel;

public interface OrderItemsStatusCodesService {
    OrderItemsStatusCodesServiceModel createOrderItemStatus(ProductStatusEnum productStatus);

    OrderItemsStatusCodesServiceModel getById(Long id);

    OrderItemsStatusCodesServiceModel getByProductStatus(String productStatus);
}
