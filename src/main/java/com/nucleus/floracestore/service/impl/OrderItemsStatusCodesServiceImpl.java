package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.error.QueryRuntimeException;
import com.nucleus.floracestore.model.entity.OrderItemsStatusCodesEntity;
import com.nucleus.floracestore.model.enums.ProductStatusEnum;
import com.nucleus.floracestore.model.service.OrderItemsStatusCodesServiceModel;
import com.nucleus.floracestore.repository.OrderItemsStatusCodesRepository;
import com.nucleus.floracestore.service.OrderItemsStatusCodesService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class OrderItemsStatusCodesServiceImpl implements OrderItemsStatusCodesService {

    private final OrderItemsStatusCodesRepository orderItemsStatusCodesRepository;

    private final ModelMapper modelMapper;

    public OrderItemsStatusCodesServiceImpl(OrderItemsStatusCodesRepository orderItemsStatusCodesRepository, ModelMapper modelMapper) {
        this.orderItemsStatusCodesRepository = orderItemsStatusCodesRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderItemsStatusCodesServiceModel createOrderItemStatus(ProductStatusEnum productStatus) {
        OrderItemsStatusCodesEntity orderItemsStatusCodesEntity = new OrderItemsStatusCodesEntity();
        orderItemsStatusCodesEntity.setProductStatus(productStatus.getLevelName());
        orderItemsStatusCodesEntity.setProductStatusDescription(productStatus.getLevelDescription());
        orderItemsStatusCodesRepository.save(orderItemsStatusCodesEntity);
        return mapToService(orderItemsStatusCodesEntity);
    }

    @Override
    public OrderItemsStatusCodesServiceModel getById(Long id) {
        return mapToService(orderItemsStatusCodesRepository
                .findById(id)
                .orElseThrow(()->new QueryRuntimeException("Couldn't find ProductStatus'")));
    }

    @Override
    public OrderItemsStatusCodesServiceModel getByProductStatus(String productStatus) {
//        ProductStatusEnum productStatusEnum = ProductStatusEnum.valueOf(productStatus);
        return mapToService(orderItemsStatusCodesRepository
                .findByProductStatus(productStatus)
                .orElseThrow(()->new QueryRuntimeException("Couldn't find ProductStatus " + productStatus)));
    }

    private OrderItemsStatusCodesServiceModel mapToService(OrderItemsStatusCodesEntity orderItemsStatusCodes) {
        return this.modelMapper.map(orderItemsStatusCodes, OrderItemsStatusCodesServiceModel.class);
    }
}
