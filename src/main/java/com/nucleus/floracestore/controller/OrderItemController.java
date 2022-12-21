package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.hateoas.OrderItemModelAssembler;
import com.nucleus.floracestore.model.dto.OrderItemsDto;
import com.nucleus.floracestore.model.service.OrderItemServiceModel;
import com.nucleus.floracestore.model.view.OrderItemViewModel;
import com.nucleus.floracestore.service.OrderItemService;
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
public class OrderItemController {

    private final OrderItemService orderItemService;
    private final ModelMapper modelMapper;
    private final OrderItemModelAssembler assembler;

    @Autowired
    public OrderItemController(OrderItemService orderItemService,
                               ModelMapper modelMapper,
                               OrderItemModelAssembler assembler) {
        this.orderItemService = orderItemService;
        this.modelMapper = modelMapper;
        this.assembler = assembler;
    }


    @PostMapping("/order-items/orders/{orderId}/products/{productId}")
    OrderItemServiceModel createOrderItem(@RequestBody OrderItemsDto model,
                                          @PathVariable Long orderId,
                                          @PathVariable Long productId) {
        return orderItemService.createOrderItem(modelMapper.map(model, OrderItemServiceModel.class), orderId, productId);
    }

    @PostMapping("/order-items")
    public ResponseEntity<EntityModel<OrderItemViewModel>> addOrderItem(@RequestBody OrderItemsDto model) {
        OrderItemServiceModel orderItemModel = orderItemService.addOrderItem(modelMapper.map(model, OrderItemServiceModel.class));
        log.info("order problem: " + orderItemModel);
        return ResponseEntity
                .created(linkTo(methodOn(OrderItemController.class).addOrderItem(modelMapper.map(orderItemModel, OrderItemsDto.class))).toUri())
                .body(assembler.toModel(mapToView(orderItemModel)));
    }

    @GetMapping("/order-items/search/orders/{orderId}")
    public ResponseEntity<CollectionModel<EntityModel<OrderItemViewModel>>> getOrderItemsByOrderId(@PathVariable Long orderId) {
        List<EntityModel<OrderItemViewModel>> orderItems = orderItemService.getAllOrderItemsByOrderId(orderId).stream()
                .map(entity -> assembler.toModel(mapToView(entity))).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(orderItems, linkTo(methodOn(OrderItemController.class).getOrderItemsByOrderId(orderId)).withSelfRel()));
    }

    @GetMapping(value = "/order-items")
    public ResponseEntity<CollectionModel<EntityModel<OrderItemViewModel>>> getAllOrderItems() {
        List<EntityModel<OrderItemViewModel>> order = orderItemService.getAllOrderItems().stream() //
                .map(entity -> assembler.toModel(mapToView(entity))).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(order, linkTo(methodOn(OrderItemController.class).getAllOrderItems()).withSelfRel()));
    }

    @GetMapping(value = "/order-items/count/orders/{orderId}")
    public ResponseEntity<Integer> getCountOrderItems(@PathVariable Long orderId) {
        int count = orderItemService.getOrderItemsCount(orderId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(count);
    }

//    @GetMapping("/orders/{orderId}/products/{productId}/items")
//    public ResponseEntity<EntityModel<OrderItemViewModel>> getOrderItemByOrderIdAndProductId(@PathVariable Long orderId, @PathVariable Long productId) {
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(assembler.toModel(converter(orderItemService.getOrderItemByProductId(orderId, productId))));
//    }

    @PutMapping("/order-items/{itemId}")
    public OrderItemServiceModel updateOrderItem(@RequestBody OrderItemsDto model, @PathVariable Long itemId) {
        return orderItemService.updateOrderItemQuantity(modelMapper.map(model, OrderItemServiceModel.class), itemId);
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/order-items/{itemId}")
    public ResponseEntity<EntityModel<OrderItemViewModel>> deleteOrderItem(@PathVariable Long itemId) {
        EntityModel<OrderItemViewModel> orderItemViewModel = assembler.toModel(mapToView(orderItemService.deleteOrderItem(itemId)));
        return ResponseEntity.status(HttpStatus.OK).body(orderItemViewModel);
    }

    private OrderItemViewModel mapToView(OrderItemServiceModel model) {
        log.info("OrderItems: " + model);
        return modelMapper.map(model, OrderItemViewModel.class);
    }

}
