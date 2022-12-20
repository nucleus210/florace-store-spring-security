package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.hateoas.OrderStatusCodesAssembler;
import com.nucleus.floracestore.model.dto.OrderStatusCodesDto;
import com.nucleus.floracestore.model.service.OrderStatusCodesServiceModel;
import com.nucleus.floracestore.model.view.OrderStatusCodesViewModel;
import com.nucleus.floracestore.service.OrderStatusCodesService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
public class OrderStatusCodesController {

    private final ModelMapper modelMapper;
    private final OrderStatusCodesService orderStatusCodesService;
    private final OrderStatusCodesAssembler assembler;
    @Autowired
    public OrderStatusCodesController(ModelMapper modelMapper,
                                      OrderStatusCodesService orderStatusCodesService,
                                      OrderStatusCodesAssembler assembler) {
        this.modelMapper = modelMapper;
        this.orderStatusCodesService = orderStatusCodesService;
        this.assembler = assembler;
    }
    @PostMapping("/order-status-codes")
    public ResponseEntity<EntityModel<OrderStatusCodesViewModel>> createOrderStatusCodes(@RequestBody OrderStatusCodesDto model)  {
        OrderStatusCodesServiceModel orderStatusCodesServiceModel = modelMapper.map(model, OrderStatusCodesServiceModel.class);
        orderStatusCodesService.save(orderStatusCodesServiceModel);
        log.info("OrderStatusCodeController: saved orderStatusCode with name " + orderStatusCodesServiceModel.getStatusCode());
        return ResponseEntity
                .created(linkTo(methodOn(OrderStatusCodesController.class).createOrderStatusCodes(model)).toUri())
                .body(assembler.toModel(mapToView(orderStatusCodesServiceModel)));
    }

    @GetMapping("/order-status-codes/{orderStatusCodeId}")
    public ResponseEntity<EntityModel<OrderStatusCodesViewModel>> getOrderStatusCodeById(@PathVariable Long orderStatusCodeId) {
        OrderStatusCodesServiceModel orderStatusCodesServiceModel =
                orderStatusCodesService.getOrderStatusCodeById(orderStatusCodeId);
        log.info("OrderStatusCodeController: get orderStatusCode by id " + orderStatusCodeId);
        return ResponseEntity
                .created(linkTo(methodOn(OrderStatusCodesController.class).getOrderStatusCodeById(orderStatusCodeId)).toUri())
                .body(assembler.toModel(mapToView(orderStatusCodesServiceModel)));
    }
    @GetMapping("/order-status-codes/{codeName}/names")
    public ResponseEntity<EntityModel<OrderStatusCodesViewModel>> getOrderStatusCodeByCodeName(@PathVariable String codeName) {
        OrderStatusCodesServiceModel orderStatusCodesServiceModel =
                orderStatusCodesService.getOrderStatusCodeByCodeName(codeName);
        log.info("OrderStatusCodeController: get orderStatusCode by code name " + codeName);
        return ResponseEntity
                .created(linkTo(methodOn(OrderStatusCodesController.class).getOrderStatusCodeByCodeName(codeName)).toUri())
                .body(assembler.toModel(mapToView(orderStatusCodesServiceModel)));
    }
    @GetMapping("/order-status-codes")
    public ResponseEntity<CollectionModel<EntityModel<OrderStatusCodesViewModel>>> getAllOrderStatusCodes() {
        List<EntityModel<OrderStatusCodesViewModel>> orderStatusCodes = orderStatusCodesService.getAllOrderStatusCodes().stream()
                .map(entity -> assembler.toModel(mapToView(entity))).toList();
        log.info("OrderStatusCodeController: get all orderStatusCodes");
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(orderStatusCodes, linkTo(methodOn(OrderStatusCodesController.class).getAllOrderStatusCodes()).withSelfRel()));
    }
    private OrderStatusCodesViewModel mapToView(OrderStatusCodesServiceModel orderStatusCodesServiceModel) {
        return modelMapper.map(orderStatusCodesServiceModel, OrderStatusCodesViewModel.class);
    }
}
