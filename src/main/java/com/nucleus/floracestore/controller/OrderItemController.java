package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.hateoas.OrderItemModelAssembler;
import com.nucleus.floracestore.model.dto.OrderItemsDto;
import com.nucleus.floracestore.model.service.OrderItemServiceModel;
import com.nucleus.floracestore.model.view.OrderItemViewModel;
import com.nucleus.floracestore.service.OrderItemService;
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

@RestController
public class OrderItemController {

    private final OrderItemService orderItemService;
    private final ModelMapper modelMapper;
    private final OrderItemModelAssembler assembler;

    @Autowired
    public OrderItemController(OrderItemService orderItemService, ModelMapper modelMapper, OrderItemModelAssembler assembler) {
        this.orderItemService = orderItemService;
        this.modelMapper = modelMapper;
        this.assembler = assembler;
    }


    @ModelAttribute("orderItemModel")
    public OrderItemsDto orderItemModel() {
        return new OrderItemsDto();
    }

    @PostMapping("/orders/{orderId}/products/{productId}/items")
    OrderItemServiceModel createOrderItem(@RequestBody OrderItemsDto model,
                                          @PathVariable Long orderId,
                                          @PathVariable Long productId) {
        return orderItemService.createOrderItem(modelMapper.map(model, OrderItemServiceModel.class), orderId, productId);
    }
    @PostMapping("/order-items")
    public ResponseEntity<EntityModel<OrderItemViewModel>> addOrderItem(@RequestBody OrderItemsDto model) {
        OrderItemServiceModel orderItemModel = orderItemService.addOrderItem(modelMapper.map(model, OrderItemServiceModel.class));
        return ResponseEntity
                .created(linkTo(methodOn(OrderItemController.class).addOrderItem(model)).toUri())
                .body(assembler.toModel(converter(orderItemModel)));
    }

    @GetMapping("/orders/{orderId}/items")
    public ResponseEntity<CollectionModel<EntityModel<OrderItemViewModel>>> getOrderItemsByOrderId(@PathVariable Long orderId) {
        List<EntityModel<OrderItemViewModel>> orderItems = orderItemService.getAllOrderItemsByOrderId(orderId).stream()
                .map(entity -> assembler.toModel(converter(entity))).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(orderItems, linkTo(methodOn(OrderItemController.class).getOrderItemsByOrderId(orderId)).withSelfRel()));
    }

    @GetMapping(value = "/order-items")
    public ResponseEntity<CollectionModel<EntityModel<OrderItemViewModel>>> getAllOrderItems() {
        List<EntityModel<OrderItemViewModel>> order = orderItemService.getAllOrderItems().stream() //
                .map(entity -> assembler.toModel(converter(entity))).toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(order, linkTo(methodOn(OrderItemController.class).getAllOrderItems()).withSelfRel()));
    }
    @GetMapping(value = "/orders/items/users/{username}/count")
    public ResponseEntity<Integer> getCountOrderItems(@PathVariable String username) {
        int count = orderItemService.getOrderItemsCount(username);
        return ResponseEntity.status(HttpStatus.OK)
                .body(count);
    }

//    @GetMapping("/orders/{orderId}/products/{productId}/items")
//    public ResponseEntity<EntityModel<OrderItemViewModel>> getOrderItemByOrderIdAndProductId(@PathVariable Long orderId, @PathVariable Long productId) {
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(assembler.toModel(converter(orderItemService.getOrderItemByProductId(orderId, productId))));
//    }

    @PutMapping("/orders/{orderId}/items/{itemId}")
    public OrderItemServiceModel updateOrderItem(@RequestBody OrderItemsDto model, @PathVariable Long itemId) {
        return orderItemService.updateOrderItemQuantity(modelMapper.map(model, OrderItemServiceModel.class), itemId);
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/orders/items/{itemId}")
    public ResponseEntity<EntityModel<OrderItemViewModel>> deleteOrderItem(@PathVariable Long itemId) {
        EntityModel<OrderItemViewModel> orderItemViewModel = assembler.toModel(converter(orderItemService.deleteOrderItem(itemId)));
        return ResponseEntity.status(HttpStatus.OK).body(orderItemViewModel);
    }

    private OrderItemViewModel converter(OrderItemServiceModel model) {
        return modelMapper.map(model, OrderItemViewModel.class);
    }

}
