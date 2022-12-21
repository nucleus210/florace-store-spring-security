package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.error.QueryRuntimeException;
import com.nucleus.floracestore.model.entity.OrderEntity;
import com.nucleus.floracestore.model.entity.RoleEntity;
import com.nucleus.floracestore.model.enums.OrderStatusCodes;
import com.nucleus.floracestore.model.enums.UserRoleEnum;
import com.nucleus.floracestore.model.service.OrderServiceModel;
import com.nucleus.floracestore.model.service.OrderStatusCodesServiceModel;
import com.nucleus.floracestore.model.service.UserServiceModel;
import com.nucleus.floracestore.repository.OrderRepository;
import com.nucleus.floracestore.service.OrderService;
import com.nucleus.floracestore.service.OrderStatusCodesService;
import com.nucleus.floracestore.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderStatusCodesService orderStatusCodesService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderStatusCodesService orderStatusCodesService,
                            UserService userService,
                            ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.orderStatusCodesService = orderStatusCodesService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Transactional

    @Override
    public OrderServiceModel createOrder(OrderServiceModel orderServiceModel, String username) {
        UserServiceModel userServiceModel = userService.findByUsername(username)
                .orElseThrow(() -> new QueryRuntimeException("Could not find user " + username));
        orderServiceModel.setUser(userServiceModel);
        OrderStatusCodesServiceModel orderStatusCodesServiceModel =
                orderStatusCodesService.getOrderStatusCodeByCodeName(OrderStatusCodes.DRAFT.getLevelName());
        orderServiceModel.setOrderStatusCode(orderStatusCodesServiceModel);
        OrderEntity orderEntity = modelMapper.map(orderServiceModel, OrderEntity.class);
        return mapToService(orderRepository.save(orderEntity));
    }

    @Override
    public void updateOrder(OrderServiceModel orderServiceModel) {
        orderRepository.save(modelMapper.map(orderServiceModel, OrderEntity.class));
    }

    @Override
    public void deleteOrder(Long id, String owner) {

    }

    @Override
    public OrderServiceModel getActiveOrderByUsername(String username) {
        OrderEntity orderEntity = orderRepository
                .findOrderByUsernameAndOrderStatusCode(username, "DRAFT")
                .orElseThrow(() -> new QueryRuntimeException("Could not find active order for user " + username));
        return mapToService(orderEntity);
    }

    @Override
    public List<OrderServiceModel> getAllOrdersItemsByUsernameAndOrderStatusCode(String username, String statusCode) {
        return orderRepository.findAllOrdersByUsernameAndOrderStatusCode(username, statusCode)
                .stream()
                .map(this::mapToService)
                .collect(Collectors.toList());


    }

    private OrderServiceModel mapToService(OrderEntity order) {
        log.info("OrderService: " + order);
        return modelMapper.map(order, OrderServiceModel.class);
    }

    @Override
    public OrderServiceModel getOrderById(Long id) {
        return orderRepository
                .findById(id)
                .map(this::mapToService)
                .orElseThrow(() -> new QueryRuntimeException("Could not find order " + id));
    }

    @Override
    public List<OrderServiceModel> getAllOrderByUsername(String username) {
        UserServiceModel userServiceModel = userService.findByUsername(username)
                .orElseThrow(() -> new QueryRuntimeException("Could not find user " + username));

        return orderRepository
                .findAllOrdersByUsername(userServiceModel.getUserId())
                .stream()
                .map(this::mapToService)
                .collect(Collectors.toList());
    }

    @Override
    public OrderServiceModel getOrderByUserId(Long id) {
        return orderRepository
                .findByOrderId(id)
                .map(this::mapToService)
                .orElseThrow(() -> new QueryRuntimeException("Could not find user " + id));
    }

    @Override
    public List<OrderServiceModel> getAllOrdersByDatePeriod(String startDate, String endDate) {
        return orderRepository
                .findByDatePeriod(startDate, endDate)
                .stream()
                .map(this::mapToService)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderServiceModel> getAllOrders() {
        return orderRepository
                .findAll()
                .stream()
                .map(this::mapToService)
                .collect(Collectors.toList());
    }

    public boolean isOwner(String userName, Long id) {
        Optional<OrderEntity> orderEntity = orderRepository.
                findByOrderId(id);
        Optional<UserServiceModel> caller = userService.
                findByUsername(userName);
        if (orderEntity.isEmpty() || caller.isEmpty()) {
            return false;
        } else {
            OrderEntity order = orderEntity.get();
            return isAdmin(caller.get()) ||
                    order.getUser().equals(userName);
        }
    }

    private boolean isAdmin(UserServiceModel user) {
        return user.
                getRoles().
                stream().
                map(RoleEntity::getRoleName).
                anyMatch(r -> r.equals("ROLE_" + UserRoleEnum.ADMIN));
    }
}