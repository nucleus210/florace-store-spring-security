package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.error.QueryRuntimeException;
import com.nucleus.floracestore.model.entity.OrderItemEntity;
import com.nucleus.floracestore.model.enums.ProductStatusEnum;
import com.nucleus.floracestore.model.service.OrderItemServiceModel;
import com.nucleus.floracestore.model.service.OrderItemsStatusCodesServiceModel;
import com.nucleus.floracestore.model.service.OrderServiceModel;
import com.nucleus.floracestore.model.service.ProductServiceModel;
import com.nucleus.floracestore.repository.OrderItemRepository;
import com.nucleus.floracestore.service.OrderItemService;
import com.nucleus.floracestore.service.OrderItemsStatusCodesService;
import com.nucleus.floracestore.service.OrderService;
import com.nucleus.floracestore.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderService orderService;
    private final ModelMapper modelMapper;
    private final OrderItemsStatusCodesService orderItemsStatusCodesService;
    private final ProductService productService;

    @Autowired
    public OrderItemServiceImpl(OrderItemRepository orderItemRepository,
                                OrderService orderService,
                                ModelMapper modelMapper,
                                OrderItemsStatusCodesService orderItemsStatusCodesService,
                                ProductService productService) {
        this.orderItemRepository = orderItemRepository;
        this.orderService = orderService;
        this.modelMapper = modelMapper;
        this.orderItemsStatusCodesService = orderItemsStatusCodesService;
        this.productService = productService;
    }

    @Override
    public OrderItemServiceModel getOrderItemById(Long id) {
        log.info("orderItem: " + id);
        return orderItemRepository
                .findByOrderItemId(id)
                .map(this::mapToService)
                .orElseThrow(()->new QueryRuntimeException("Could not find order item with orderItemId " + id));
    }

    @Override
    public OrderItemServiceModel getOrderItemByUserId(Long id) {
        return null;
    }

    @Override
    public OrderItemServiceModel getOrderItemByOrderIdAndProductId(Long orderId, Long productId) {
     OrderItemEntity orderItem = orderItemRepository.findOrderItemsByOrderIdAndProductId(orderId, productId)
             .orElse(null);
     if(orderItem == null) {
         return null;
     }
        return mapToService(orderItem);
    }

    @Override
    public List<OrderItemServiceModel> getAllOrderItems() {
        return this.orderItemRepository.findAll()
                .stream().map(this::mapToService).collect(Collectors.toList());
    }

    @Override
    public List<OrderItemServiceModel> getAllOrderItemsByOrderId(Long orderId) {
        return this.orderItemRepository.findAllOrderItemsByOrderId(orderId)
                .stream().map(this::mapToService).collect(Collectors.toList());
    }

    @Override
    public int getOrderItemsCount(Long orderId) {
        return orderItemRepository.findOrderItemsCountByOrderId(orderId);
    }

    @Transactional
    @Override
    public OrderItemServiceModel addOrderItem(OrderItemServiceModel orderItemServiceModel) {
        OrderItemsStatusCodesServiceModel orderItemsStatusCodesServiceModel =
                orderItemsStatusCodesService.getByProductStatus(ProductStatusEnum.IN_STOCK.getLevelName());
        orderItemServiceModel.setOrderItemStatusCode(orderItemsStatusCodesServiceModel);
        OrderItemEntity orderItemEntity = orderItemRepository.save(modelMapper.map(orderItemServiceModel, OrderItemEntity.class));
        return modelMapper.map(orderItemEntity, OrderItemServiceModel.class);
    }

    @Override
    public OrderItemServiceModel updateOrderItem(OrderItemServiceModel orderItemServiceModel) {
        OrderItemEntity orderItemEntity = orderItemRepository.findById(orderItemServiceModel.getOrderItemId()).
                orElseThrow(() -> new QueryRuntimeException("Could not find order item with id " + orderItemServiceModel.getOrderItemId()));
        orderItemEntity.setOrderItemQuantity(orderItemServiceModel.getOrderItemQuantity());
        orderItemRepository.saveAndFlush(orderItemEntity);
        return mapToService(orderItemRepository.saveAndFlush(orderItemEntity));
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
        OrderItemEntity entity = orderItemRepository.findByOrderItemId(itemId).orElseThrow(() -> new QueryRuntimeException("Order item not found"));
        orderItemRepository.delete(entity);
        return modelMapper.map(entity, OrderItemServiceModel.class);
    }

    private OrderItemServiceModel mapToService(OrderItemEntity orderItem) {
        return this.modelMapper.map(orderItem, OrderItemServiceModel.class);
    }
}
