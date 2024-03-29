package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.service.OrderItemServiceModel;

import java.util.List;

public interface OrderItemService {
    int getOrderItemsCount(Long orderId);
    OrderItemServiceModel getOrderItemById(Long id);
    OrderItemServiceModel getOrderItemByUserId(Long id);
    OrderItemServiceModel getOrderItemByOrderIdAndProductId(Long orderId,Long productId);
    List<OrderItemServiceModel> getAllOrderItems();
    List<OrderItemServiceModel> getAllOrderItemsByOrderId(Long orderId);
    OrderItemServiceModel addOrderItem(OrderItemServiceModel orderItemServiceModel);
    OrderItemServiceModel updateOrderItem(OrderItemServiceModel orderItemServiceModel);
    OrderItemServiceModel updateOrderItemQuantity(OrderItemServiceModel orderItemServiceModel, Long productId);
    OrderItemServiceModel deleteOrderItem(Long itemId);
}
