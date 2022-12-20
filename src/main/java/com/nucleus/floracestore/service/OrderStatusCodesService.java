package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.enums.OrderStatusCodes;
import com.nucleus.floracestore.model.service.OrderStatusCodesServiceModel;

import java.util.List;

public interface OrderStatusCodesService {

    OrderStatusCodesServiceModel initializeOrderStatusCodesFromEnum(OrderStatusCodes orderStatusCodes);
    OrderStatusCodesServiceModel getOrderStatusCodeById(Long id);
    OrderStatusCodesServiceModel getOrderStatusCodeByCodeName(String code);

    List<OrderStatusCodesServiceModel> getAllOrderStatusCodes();

    OrderStatusCodesServiceModel save(OrderStatusCodesServiceModel orderStatusCodesServiceModel);
}
