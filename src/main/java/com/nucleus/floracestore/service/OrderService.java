package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.service.OrderServiceModel;
import org.springframework.beans.PropertyValues;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    OrderServiceModel getOrderById(Long id);
    List<OrderServiceModel> getAllOrderByUsername(String username);
    OrderServiceModel getOrderByUserId(Long id);
    List<OrderServiceModel> getAllOrdersByDatePeriod(String startDate, String endDate);
    List<OrderServiceModel> getAllOrders();
    OrderServiceModel createOrder(OrderServiceModel orderServiceModel, String owner);
    void updateOrder(OrderServiceModel orderServiceModel);
    void deleteOrder(Long id, String owner);

    Optional<OrderServiceModel> getActiveOrderByUsername(String username);
}
