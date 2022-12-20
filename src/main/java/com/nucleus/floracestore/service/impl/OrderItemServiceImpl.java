package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.error.QueryRuntimeException;
import com.nucleus.floracestore.model.entity.OrderEntity;
import com.nucleus.floracestore.model.entity.OrderItemEntity;
import com.nucleus.floracestore.model.entity.ProductEntity;
import com.nucleus.floracestore.model.enums.ProductStatusEnum;
import com.nucleus.floracestore.model.service.OrderItemServiceModel;
import com.nucleus.floracestore.model.service.OrderItemsStatusCodesServiceModel;
import com.nucleus.floracestore.repository.OrderItemRepository;
import com.nucleus.floracestore.repository.OrderRepository;
import com.nucleus.floracestore.service.OrderItemService;
import com.nucleus.floracestore.service.OrderItemsStatusCodesService;
import com.nucleus.floracestore.service.ProductService;
import com.nucleus.floracestore.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final OrderItemsStatusCodesService orderItemsStatusCodesService;
    private final ProductService productService;

    @Autowired
    public OrderItemServiceImpl(OrderItemRepository orderItemRepository,
                                OrderRepository orderRepository,
                                UserService userService,
                                ModelMapper modelMapper,
                                OrderItemsStatusCodesService orderItemsStatusCodesService,
                                ProductService productService) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.orderItemsStatusCodesService = orderItemsStatusCodesService;
        this.productService = productService;
    }

    @Override
    public OrderItemServiceModel getOrderItemById(Long id) {
        log.info("orderItem: " + id);
        return orderItemRepository.findByOrderItemId(id).map(this::mapToService).get();
    }

    @Override
    public OrderItemServiceModel getOrderItemByUserId(Long id) {
        return null;
    }

//    @Override
//    public OrderItemServiceModel getOrderItemByProductId(Long orderId, Long productId) {
//        OrderItemEntity orderItemEntity = orderItemRepository.findByOrderIdAndProductId(orderId, productId)
//                .orElseThrow(()->new QueryRuntimeException("Could not find order item with productId " + productId));
//        return mapToService(orderItemEntity);
//    }

    @Override
    public List<OrderItemServiceModel> getAllOrderItems() {
        return this.orderItemRepository.findAll()
                .stream().map(this::mapToService).collect(Collectors.toList());
    }

    @Override
    public List<OrderItemServiceModel> getAllOrderItemsByOrderId(Long orderId) {
        return  this.orderItemRepository.findAllOrderItemsByOrderId(orderId)
                .stream().map(this::mapToService).collect(Collectors.toList());
    }

    @Override
    public int getOrderItemsCount(String username) {
        OrderEntity order = orderRepository.findOrderByUsernameAndOrderStatusCode(username, "DRAFT")
                .orElseThrow(()->new QueryRuntimeException("Could not find order with status code Draft"));
        return orderItemRepository.findOrderItemsCountByOrderId(order.getOrderId());
    }

    @Override
    public OrderItemServiceModel addOrderItem(OrderItemServiceModel orderItemServiceModel) {
        OrderItemsStatusCodesServiceModel orderItemsStatusCodesServiceModel =
                orderItemsStatusCodesService.getByProductStatus(ProductStatusEnum.IN_STOCK.getLevelName());
        orderItemServiceModel.setOrderItemStatusCode(orderItemsStatusCodesServiceModel);
        OrderItemEntity orderItemEntity = modelMapper.map(orderItemServiceModel, OrderItemEntity.class);
        orderItemRepository.save(orderItemEntity);
        return orderItemServiceModel;
    }

    @Override
    public OrderItemServiceModel createOrderItem(OrderItemServiceModel orderItemServiceModel, Long orderId, Long productId) {
        OrderEntity orderEntity = orderRepository.findByOrderId(orderId).orElseThrow(()->new QueryRuntimeException("Order entity not found"));
        ProductEntity productEntity = modelMapper.map(productService.getProductById(productId), ProductEntity.class);
        OrderItemsStatusCodesServiceModel orderItemsStatusCodesServiceModel = orderItemsStatusCodesService
                .getByProductStatus(ProductStatusEnum.IN_STOCK.getLevelName());
//        orderItemServiceModel.setOrder(orderEntity);
//        orderItemServiceModel.setProduct(productEntity);
        orderItemServiceModel.setOrderItemStatusCode(orderItemsStatusCodesServiceModel);
        orderItemServiceModel.setOrderItemPrice(productEntity.getUnitSellPrice());
        OrderItemEntity orderItemEntity = modelMapper.map(orderItemServiceModel, OrderItemEntity.class);
        orderItemRepository.save(orderItemEntity);
        return orderItemServiceModel;
    }

    @Override
    public OrderItemServiceModel updateOrderItemQuantity(OrderItemServiceModel orderItemServiceModel, Long itemId) {
        OrderItemEntity orderItemEntity = orderItemRepository.findById(itemId).orElseThrow();
        orderItemEntity.setOrderItemQuantity(orderItemServiceModel.getOrderItemQuantity());
         modelMapper.map(orderItemRepository.save(orderItemEntity), OrderItemServiceModel.class);
        return modelMapper.map(orderItemRepository.findById(itemId), OrderItemServiceModel.class);
    }

    @Override
    public OrderItemServiceModel deleteOrderItem(Long itemId) {
        log.info(String.valueOf(itemId));
        OrderItemEntity entity = orderItemRepository.findByOrderItemId(itemId).orElseThrow(()->new QueryRuntimeException("Order item not found"));
        orderItemRepository.delete(entity);
        return modelMapper.map(entity, OrderItemServiceModel.class);
    }

    private OrderItemServiceModel mapToService(OrderItemEntity orderItem) {
        return this.modelMapper.map(orderItem, OrderItemServiceModel.class);
    }

}
