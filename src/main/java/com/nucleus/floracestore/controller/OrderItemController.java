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
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @PostMapping("/order-items")
    public ResponseEntity<EntityModel<OrderItemViewModel>> addOrderItem(@RequestBody OrderItemsDto model) {
        log.debug("OrderItemServiceModel: addOrderItem");

        OrderItemServiceModel orderItem = orderItemService.addOrderItem(modelMapper.map(model, OrderItemServiceModel.class));
        return ResponseEntity
                .created(linkTo(methodOn(OrderItemController.class).addOrderItem(modelMapper.map(model, OrderItemsDto.class))).toUri())
                .body(assembler.toModel(mapToView(orderItem)));
    }

    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @GetMapping("/order-items/search/orders/{orderId}")
    public ResponseEntity<CollectionModel<EntityModel<OrderItemViewModel>>> getOrderItemsByOrderId(@PathVariable Long orderId) {
        List<EntityModel<OrderItemViewModel>> orderItems = orderItemService.getAllOrderItemsByOrderId(orderId).stream()
                .map(entity -> assembler.toModel(mapToView(entity))).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(orderItems, linkTo(methodOn(OrderItemController.class).getOrderItemsByOrderId(orderId)).withSelfRel()));
    }
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @GetMapping("/order-items/search/orders/{orderId}/products/{productId}")
    public ResponseEntity<EntityModel<OrderItemViewModel>> getOrderItemByOrderIdAndProductId(@PathVariable Long orderId, @PathVariable Long productId) {
        OrderItemServiceModel orderItem = orderItemService.getOrderItemByOrderIdAndProductId(orderId, productId);

        return ResponseEntity
                .created(linkTo(methodOn(OrderItemController.class).getOrderItemByOrderIdAndProductId(orderId, productId)).toUri())
                .body(assembler.toModel(mapToView(orderItem)));
    }

    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @GetMapping(value = "/order-items")
    public ResponseEntity<CollectionModel<EntityModel<OrderItemViewModel>>> getAllOrderItems() {
        List<EntityModel<OrderItemViewModel>> order = orderItemService.getAllOrderItems().stream() //
                .map(entity -> assembler.toModel(mapToView(entity))).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(order, linkTo(methodOn(OrderItemController.class).getAllOrderItems()).withSelfRel()));
    }

    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @GetMapping(value = "/order-items/count/orders/{orderId}")
    public ResponseEntity<Integer> getCountOrderItems(@PathVariable Long orderId) {
        int count = orderItemService.getOrderItemsCount(orderId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(count);
    }

    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
    @PutMapping("/order-items/{itemId}")
    public OrderItemServiceModel updateOrderItem(@RequestBody OrderItemsDto model, @PathVariable Long itemId) {
        return orderItemService.updateOrderItemQuantity(modelMapper.map(model, OrderItemServiceModel.class), itemId);
    }

    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK_USER')")
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
