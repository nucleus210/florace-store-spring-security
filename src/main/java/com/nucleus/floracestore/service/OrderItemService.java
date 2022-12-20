package com.nucleus.floracestore.service;

import com.nucleus.floracestore.model.service.OrderItemServiceModel;

import java.util.List;

public interface OrderItemService {
    OrderItemServiceModel getOrderItemById(Long id);
    OrderItemServiceModel getOrderItemByUserId(Long id);

    OrderItemServiceModel getOrderItemByProductId(Long orderId,Long productId);
    List<OrderItemServiceModel> getAllOrderItems();
    List<OrderItemServiceModel> getAllOrderItemsByOrderId(Long orderId);

    int getOrderItemsCount(String username);
    OrderItemServiceModel createOrderItem(OrderItemServiceModel orderItemServiceModel, Long orderId, Long productId);
    OrderItemServiceModel updateOrderItemQuantity(OrderItemServiceModel orderItemServiceModel, Long productId);
    OrderItemServiceModel deleteOrderItem(Long itemId);
}
