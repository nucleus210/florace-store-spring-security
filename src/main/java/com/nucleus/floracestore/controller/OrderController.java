package com.nucleus.floracestore.controller;

import com.nucleus.floracestore.hateoas.OrderModelAssembler;
import com.nucleus.floracestore.model.dto.OrderDto;
import com.nucleus.floracestore.model.service.OrderServiceModel;
import com.nucleus.floracestore.model.view.OrderViewModel;
import com.nucleus.floracestore.service.OrderService;
import com.nucleus.floracestore.service.impl.MyUserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Slf4j
public class OrderController {
    private final OrderService orderService;
    private final ModelMapper modelMapper;
    private final OrderModelAssembler assembler;


    @Autowired
    public OrderController(OrderService orderService,
                           ModelMapper modelMapper,
                           OrderModelAssembler assembler) {
        this.orderService = orderService;
        this.modelMapper = modelMapper;
        this.assembler = assembler;

    }

    private String getCurrentLoggedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Principal: " + authentication.getName());
        return authentication.getName();
    }

    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN') or hasRole('USER') or hasRole('FACEBOOK_USER')")
    @PostMapping("/orders")
    public ResponseEntity<EntityModel<OrderViewModel>> createOrder(@RequestBody OrderViewModel model) {
        OrderServiceModel orderServiceModel =
                orderService.createOrder(modelMapper.map(model, OrderServiceModel.class), getCurrentLoggedUsername());
        log.info("OrderController: created order with id: " + orderServiceModel.getOrderId());
        return ResponseEntity
                .created(linkTo(methodOn(OrderController.class).createOrder(model)).toUri())
                .body(assembler.toModel(mapToView(orderServiceModel)));
    }

    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN') or hasRole('USER') or hasRole('FACEBOOK_USER')")
    @GetMapping("/orders/{id}")
    public ResponseEntity<EntityModel<OrderViewModel>> getOrderById(@PathVariable Long id) {
        OrderServiceModel orderServiceModel = orderService.getOrderById(id);
        log.info("OrderController: get order by id: " + id);
        return ResponseEntity
                .created(linkTo(methodOn(OrderController.class).getOrderById(id)).toUri())
                .body(assembler.toModel(mapToView(orderServiceModel)));
    }

    @GetMapping("/orders/active/users/{username}")
    public ResponseEntity<EntityModel<OrderViewModel>> getActiveOrderByUsername(@PathVariable String username) {

        OrderServiceModel orderServiceModel = orderService.getActiveOrderByUsername(username);
        log.info("OrderController: get order by username: " + username);
        return ResponseEntity
                .created(linkTo(methodOn(OrderController.class).getActiveOrderByUsername(username)).toUri())
                .body(assembler.toModel(mapToView(orderServiceModel)));
    }

    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN') or hasRole('USER') or hasRole('FACEBOOK_USER')")
    @GetMapping("/orders/search/users/{username}")
    public ResponseEntity<CollectionModel<EntityModel<OrderViewModel>>> getAllOrdersByUsername(@PathVariable String username) {
        List<EntityModel<OrderViewModel>> order = orderService.getAllOrderByUsername(username).stream() //
                .map(entity -> assembler.toModel(mapToView(entity))).toList();
        log.info("OrderController: get all orders by username: " + username);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(order, linkTo(methodOn(OrderController.class).getAllOrdersByUsername(username)).withSelfRel()));
    }
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN') or hasRole('USER') or hasRole('FACEBOOK_USER')")
    @GetMapping(value = "/orders")
    public ResponseEntity<CollectionModel<EntityModel<OrderViewModel>>> getAllOrders() {
        List<EntityModel<OrderViewModel>> order = orderService.getAllOrders().stream() //
                .map(entity -> assembler.toModel(mapToView(entity))).toList();
        log.info("OrderController: get orders");

        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(order, linkTo(methodOn(OrderController.class).getAllOrders()).withSelfRel()));
    }
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN') or hasRole('USER') or hasRole('FACEBOOK_USER')")
    @GetMapping(value = "/orders/dates")
    public ResponseEntity<CollectionModel<EntityModel<OrderViewModel>>> getAllOrdersByDates(String startDate, String endDate) {
        List<EntityModel<OrderViewModel>> order = orderService.getAllOrdersByDatePeriod(startDate, endDate).stream() //
                .map(entity -> assembler.toModel(mapToView(entity))).toList();
        log.info("OrderController: get orders by date period from: " + startDate + " to " + endDate);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CollectionModel.of(order, linkTo(methodOn(OrderController.class).getAllOrdersByDates(startDate, endDate)).withSelfRel()));

    }
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN') or hasRole('USER') or hasRole('FACEBOOK_USER')")
    @PutMapping("/orders/{id}")
    public ResponseEntity<EntityModel<OrderViewModel>> updateOrder(@RequestBody OrderDto model) {
        OrderServiceModel orderServiceModel = orderService.getOrderById(model.getOrderId());
        log.info("OrderController: update order with id: " + orderServiceModel.getOrderId());
        return ResponseEntity
                .created(linkTo(methodOn(OrderController.class).updateOrder(model)).toUri())
                .body(assembler.toModel(mapToView(orderServiceModel)));
    }

    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN') or hasRole('USER') or hasRole('FACEBOOK_USER')")
    @DeleteMapping("/orders/{id}")
    public String deleteProduct(@PathVariable Long id, MyUserPrincipal principal) {
        orderService.deleteOrder(id, principal.getUsername());
        return "redirect:/home";
    }

    private OrderViewModel mapToView(OrderServiceModel model) {
        return modelMapper.map(model, OrderViewModel.class);
    }


}
