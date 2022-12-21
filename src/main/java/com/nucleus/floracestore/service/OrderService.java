package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.service.OrderServiceModel;

import java.util.List;

public interface OrderService {

    OrderServiceModel getOrderById(Long id);

    OrderServiceModel getActiveOrderByUsername(String username);

    List<OrderServiceModel> getAllOrdersItemsByUsernameAndOrderStatusCode(String username, String statusCode);

    List<OrderServiceModel> getAllOrderByUsername(String username);

    OrderServiceModel getOrderByUserId(Long id);

    List<OrderServiceModel> getAllOrdersByDatePeriod(String startDate, String endDate);

    List<OrderServiceModel> getAllOrders();

    OrderServiceModel createOrder(OrderServiceModel orderServiceModel, String owner);

    void updateOrder(OrderServiceModel orderServiceModel);

    void deleteOrder(Long id, String owner);
}
