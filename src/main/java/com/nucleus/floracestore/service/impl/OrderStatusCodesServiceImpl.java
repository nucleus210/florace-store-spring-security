package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.error.QueryRuntimeException;
import com.nucleus.floracestore.model.entity.OrderStatusCodesEntity;
import com.nucleus.floracestore.model.enums.OrderStatusCodes;
import com.nucleus.floracestore.model.service.OrderStatusCodesServiceModel;
import com.nucleus.floracestore.repository.OrderStatusCodesRepository;
import com.nucleus.floracestore.service.OrderStatusCodesService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderStatusCodesServiceImpl implements OrderStatusCodesService {
    private final OrderStatusCodesRepository orderStatusCodesRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderStatusCodesServiceImpl(OrderStatusCodesRepository orderStatusCodesRepository, ModelMapper modelMapper) {
        this.orderStatusCodesRepository = orderStatusCodesRepository;
        this.modelMapper = modelMapper;
    }
    @Override
    public OrderStatusCodesServiceModel initializeOrderStatusCodesFromEnum(OrderStatusCodes orderStatusCodes) {
        OrderStatusCodesEntity orderStatusCodesEntity = new OrderStatusCodesEntity();
        orderStatusCodesEntity.setStatusCode(orderStatusCodes.getLevelName());
        orderStatusCodesEntity.setStatusDescription(orderStatusCodes.getLevelDescription());
        orderStatusCodesRepository.save(orderStatusCodesEntity);
        return mapToService(orderStatusCodesEntity);
    }

    @Override
    public OrderStatusCodesServiceModel save(OrderStatusCodesServiceModel orderStatusCodesServiceModel) {
        OrderStatusCodesEntity orderStatusCodesEntity = new OrderStatusCodesEntity();
        orderStatusCodesEntity.setStatusCode(orderStatusCodesServiceModel.getStatusCode());
        orderStatusCodesEntity.setStatusDescription(orderStatusCodesServiceModel.getStatusDescription());
        orderStatusCodesRepository.save(orderStatusCodesEntity);
        return mapToService(orderStatusCodesEntity);
    }
    @Override
    public OrderStatusCodesServiceModel getOrderStatusCodeById(Long orderStatusCodeId) {
       OrderStatusCodesEntity orderStatusCodesEntity = orderStatusCodesRepository.findById(orderStatusCodeId)
                .orElseThrow(() -> new QueryRuntimeException("No such order status code with id " + orderStatusCodeId));
        return mapToService(orderStatusCodesEntity);
    }
    @Override
    public OrderStatusCodesServiceModel getOrderStatusCodeByCodeName(String code) {
        OrderStatusCodesEntity orderStatusCodesEntity = orderStatusCodesRepository.findByStatusCode(code)
                .orElseThrow(() -> new QueryRuntimeException("No such order status code with code " + code));
        return mapToService(orderStatusCodesEntity);
    }

    @Override
    public List<OrderStatusCodesServiceModel> getAllOrderStatusCodes() {
        return orderStatusCodesRepository
                .findAll()
                .stream()
                .map(this::mapToService)
                .collect(Collectors.toList());

    }

    private OrderStatusCodesServiceModel mapToService(OrderStatusCodesEntity order) {
        return this.modelMapper.map(order, OrderStatusCodesServiceModel.class);
    }
}
